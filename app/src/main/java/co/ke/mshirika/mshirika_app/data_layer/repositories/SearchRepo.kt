package co.ke.mshirika.mshirika_app.data_layer.repositories

import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.*
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepo @Inject constructor(
    private val searchService: SearchService,
    private val clientsService: ClientsService,
    private val groupsService: GroupsService,
    private val loansService: LoansService,
    private val repository: PreferencesStoreRepository
) {

    private val clientsList = mutableListOf<Client>()
    private val groupsList = mutableListOf<Group>()
    private val loansList = mutableListOf<LoanAccount>()

    private val _centers = MutableStateFlow<PagingData<Center>>(PagingData.empty())
    private val _clients = MutableStateFlow<PagingData<Client>>(PagingData.empty())
    private val _groups = MutableStateFlow<PagingData<Group>>(PagingData.empty())
    private val _loans = MutableStateFlow<PagingData<LoanAccount>>(PagingData.empty())

    val centers = _centers.asStateFlow()
    val clients = _clients.asStateFlow()
    val groups = _groups.asStateFlow()
    val loans = _loans.asStateFlow()

    suspend fun search(query: String) {
        searchClients(query)
        searchLoans(query)
        searchGroups(query)
    }

    private suspend fun searchClients(query: String) = withContext(IO) {
        respond {
            searchService.search(
                map = headers(),
                query = query,
                resource = SearchService.CLIENTS
            )
        }.also {
            execute(it) { clientRespondent() }
        }
    }

    private suspend fun searchGroups(query: String) = withContext(IO) {
        respond {
            searchService.search(
                map = headers(),
                query = query,
                resource = SearchService.GROUPS
            )
        }.let { execute(it) { groupRespondent() } }
    }

    private suspend fun searchLoans(query: String) = withContext(IO) {
        respond {
            searchService.search(
                map = headers(),
                query = query,
                resource = SearchService.LOANS
            )
        }.let { execute(it) { loanRespondent() } }
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
        respond {
            groupsService.group(headers(), entityId)
        }.let { result ->
            if (result is Success) {
                result.data?.let {
                    groupsList += it
                    _groups.value = PagingData.from(groupsList)
                }
            }
        }
    }

    private suspend fun Search.loanRespondent() = withContext(IO) {
        //clientsService.account(headers, entityId)
        respondWithSuccess {
            loansService.loan(headers(), entityId)
        }?.let { result ->
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

    private suspend fun headers() = repository.authKey().headers
}