package co.ke.mshirika.mshirika_app.data_layer.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.SearchDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Search
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.SearchResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.ClientsPagingSource
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.PagingSourceUtil.pagingConfig
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepo @Inject constructor(
    private val searchService: SearchService,
    private val groupsService: GroupsService,
    private val loansService: LoansService,
    private val dao: SearchDao,
    private val store: PreferencesStoreRepository
) {

    private val loansList = mutableListOf<ConservativeLoanAccount>()
    private val _loans = MutableStateFlow<PagingData<ConservativeLoanAccount>>(PagingData.empty())

    fun clientes(query: String) = Pager(
        config = pagingConfig(),
        pagingSourceFactory = {
            ClientsPagingSource(
                store = store,
                service = searchService,
                query = query
            )
        }
    ).flow

    fun clients(consulta: String) = Pager(
        pagingConfig(),
        pagingSourceFactory = { dao.clients(consulta = consulta) }
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun grupos(query: String) = channelFlow {
        withContext(IO) {
            val result: SearchResponse = searchGroups(query) ?: return@withContext

            Log.d(TAG, "searching groups ${result.toList().joinToString("\n")}")
            val list = mutableListOf<Grupo>()
            for (search in result) {
                val group = search.groupRespondent() ?: continue
                list += group
            }
            send(PagingData.from(list))
        }
    }

    fun groups(query: String) = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { dao.clients(query) }
    ).flow

    @OptIn(ExperimentalCoroutinesApi::class)
    fun prestamos(consulta: String) = channelFlow {
        withContext(IO) {
            val result = searchLoans(consulta) ?: return@withContext

            val loans = mutableListOf<ConservativeLoanAccount>()
            for (search in result) {
                val loan = search.loanRespondent() ?: continue
                loans += loan
            }
            send(PagingData.from(loans))
        }
    }

    fun loans(consulta: String) = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { dao.loans(consulta) }
    )

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
        }
    }

    private suspend fun Search.groupRespondent() = withContext(IO) {
        respondWithSuccess {
            groupsService.group(headers(), entityId)
        }
    }

    private suspend fun Search.loanRespondent() = withContext(IO) {
        respondWithSuccess {
            loansService.loan(headers(), entityId)
        }?.also { result ->
            loansList += result
            _loans.value = PagingData.from(loansList)
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