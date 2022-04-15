package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.*
import co.ke.mshirika.mshirika_app.remote.response.SearchResponse
import co.ke.mshirika.mshirika_app.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.remote.services.LoansService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SearchRepo @Inject constructor(
    private val searchService: SearchService,
    private val clientsService: ClientsService,
    private val groupsService: GroupsService,
    private val loansService: LoansService
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

    lateinit var headers: Map<String, String>

    suspend fun search(headers: Map<String, String>, query: String) {
        this.headers = headers
        searchClients(query)
        searchLoans(query)
        searchGroups(query)
    }

    private suspend fun searchClients(query: String) {
        respond {
            searchService.search(
                map = headers,
                query = query,
                resource = SearchService.CLIENTS
            )
        }.also {
            execute(it) { clientRespondent() }
        }
    }

    private suspend fun searchGroups(query: String) {
        respond {
            searchService.search(
                map = headers,
                query = query,
                resource = SearchService.GROUPS
            )
        }.also { execute(it) { groupRespondent() } }
    }

    private suspend fun searchLoans(query: String) {
        respond {
            searchService.search(
                map = headers,
                query = query,
                resource = SearchService.LOANS
            )
        }.also { execute(it) { loanRespondent() } }
    }

    private suspend fun Search.clientRespondent() {
        respond {
            clientsService.client(headers, entityId)
        }.also { result ->
            if (result is Success) {
                result.data?.let {
                    clientsList += it
                    _clients.value = PagingData.from(clientsList)
                }
            }
        }
    }

    private suspend fun Search.groupRespondent() {
        respond {
            groupsService.group(headers, entityId)
        }.also { result ->
            if (result is Success) {
                result.data?.let {
                    groupsList += it
                    _groups.value = PagingData.from(groupsList)
                }
            }
        }
    }

    private suspend fun Search.loanRespondent() {
        respond {
            //clientsService.account(headers, entityId)
            loansService.loan(headers, entityId)
        }.also { result ->
            if (result is Success) {
                result.data?.let {
                    loansList += it
                    _loans.value = PagingData.from(loansList)
                }
            }
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
}