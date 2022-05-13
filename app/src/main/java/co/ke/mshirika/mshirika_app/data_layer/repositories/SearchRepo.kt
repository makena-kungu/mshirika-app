package co.ke.mshirika.mshirika_app.data_layer.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.liveData
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.ClientsPagingSourceWithQuery
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Center
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Search
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.response.SearchResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepo @Inject constructor(
    private val searchService: SearchService,
    private val clientsService: ClientsService,
    private val groupsService: GroupsService,
    private val loansService: LoansService,
    private val store: PreferencesStoreRepository
) {

    private val _query = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val clientsList = mutableListOf<Client>()
    private val groupsList = mutableListOf<Group>()
    private val loansList = mutableListOf<ConservativeLoanAccount>()

    val loadingLoans = Channel<Boolean>()
    val loadingGroups = Channel<Boolean>()

    private val _centers = MutableStateFlow<PagingData<Center>>(PagingData.empty())
    private val _clients = MutableStateFlow<PagingData<Client>>(PagingData.empty())
    private val _groups = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    private val _loans = MutableStateFlow<PagingData<ConservativeLoanAccount>>(PagingData.empty())

    val centers = _centers.asStateFlow()

    fun clientes(query: String) = Pager(
        config = pagingConfig(),
        pagingSourceFactory = {
            ClientsPagingSourceWithQuery(
                store = store,
                service = searchService,
                query = query
            )
        }
    ).liveData

    @OptIn(ExperimentalCoroutinesApi::class)
    fun grupos(query: String) = channelFlow {
        withContext(IO) {
            loadingGroups.send(true)
            val result: SearchResponse = searchGroups(query) ?: return@withContext

            Log.d(TAG, "searching groups ${result.toList().joinToString("\n")}")
            val list = mutableListOf<Group>()
            for (search in result) {
                val group = search.groupRespondent() ?: continue
                list += group
            }
            send(PagingData.from(list))
        }
        loadingGroups.send(false)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun prestamos(consulta: String) = channelFlow {
        withContext(IO) {
            loadingLoans.send(true)
            val result = searchLoans(consulta) ?: return@withContext

            val loans = mutableListOf<ConservativeLoanAccount>()
            for (search in result) {
                val loan = search.loanRespondent() ?: continue
                loans += loan
            }
            send(PagingData.from(loans))
        }
        loadingLoans.send(false)
    }

    fun search(query: String) {
        _query.tryEmit(query)
        //searchLoans(query)
        //searchGroups(query)
        /*withContext(IO) {

        }*/
    }

    suspend fun searchClients(query: String) = withContext(IO) {
        respond {
            searchService.searchClient(
                headers(),
                sqlSearch = SearchService.clients(query)
            )
        }
    }

    private suspend fun searchGroups(query: String) = withContext(IO) {
        respondWithSuccess {
            searchService.search(
                map = headers(),
                query = query,
                resource = SearchService.GROUPS
            )
        }//.also { execute(it) { groupRespondent() } }
    }

    private suspend fun searchLoans(query: String) = withContext(IO) {
        respondWithSuccess {
            searchService.search(
                map = headers(),
                query = query,
                resource = SearchService.LOANS
            )
        }//.also { execute(it) { loanRespondent() } }
    }

    private suspend fun Search.clientRespondent() = withContext(IO) {
        respond {
            clientsService.client(headers(), entityId)
        }.let { result ->
            if (result is Success) {
                result.data?.let {
                    clientsList += it
                    _clients.value = PagingData.from(clientsList)
                }
            }
        }
    }

    private suspend fun Search.groupRespondent() = withContext(IO) {
        respondWithSuccess {
            groupsService.group(headers(), entityId)
        }
    }

    private suspend fun Search.loanRespondent() = withContext(IO) {
        //clientsService.account(headers, entityId)
        respondWithSuccess {
            loansService.loan(headers(), entityId)
        }?.also { result ->
            loansList += result
            _loans.value = PagingData.from(loansList)
        }
    }

    private suspend fun execute(
        outcome: Outcome<SearchResponse>,
        block: suspend Search.() -> Unit
    ) {
        if (outcome is Success) {
            outcome.data?.run {
                forEach { block(it) }
            }
        }
    }

    suspend fun repaymentSchedule(loanId: Int) = withContext(IO) {
        respondWithSuccess { loansService.loanRepaymentSchedule(headers(), loanId) }
    }

    private suspend fun headers() = store.authKey().headers

    companion object {
        private const val TAG = "SearchRepo"
    }
}