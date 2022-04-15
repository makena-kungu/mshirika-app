package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.Search
import co.ke.mshirika.mshirika_app.data.response.Search.Companion.ENTITY_CLIENT
import co.ke.mshirika.mshirika_app.pagingSource.ClientsPagingSource
import co.ke.mshirika.mshirika_app.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.remote.services.LoansService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.*
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClientsRepo @Inject constructor(
    private val service: ClientsService,
    private val loansService: LoansService,
    private val searchService: SearchService,
    private val prefRepo: PreferencesStoreRepository
) {
    private val searchedClientList = mutableListOf<Client>()
    private val loanAccounts = mutableListOf<LoanAccount>()

    private val _accounts = MutableStateFlow<Outcome<AccountsResponse>>(Empty())
    private val _loans = MutableStateFlow<List<LoanAccount>>(emptyList())
    private val _searchedClients = MutableStateFlow<Outcome<PagingData<Client>>>(Empty())
    private val _transactions = MutableStateFlow<Outcome<TransactionResponse>>(Empty())

    val accounts
        get() = _accounts
    val loans
        get() = _loans
    val searchedClients
        get() = _searchedClients.asStateFlow()
    val transactions
        get() = _transactions.asStateFlow()


    val clients: Flow<PagingData<Client>>
        get() = flow {
            val key = prefRepo.authKey()
            Pager(
                config = pagingConfig(),
                pagingSourceFactory = {
                    ClientsPagingSource(
                        authKey = key,
                        service = service
                    )
                }
            ).flow
        }

    suspend fun accounts(clientId: Int) {
        _accounts.value = Loading()
        respond {
            service.accounts(headers(), clientId)
        }.also {
            _accounts.value = it
        }
    }

    suspend fun loans(loanId: Int) {
        respond {
            loansService.loan(
                headers(),
                loanId
            )
        }.apply {
            if (this is Success) data?.let { loanAccounts += it }
            _loans.value = loanAccounts
        }
    }

    suspend fun search(query: String) {
        _searchedClients.value = Loading()
        val headers = headers()
        respond {
            searchService.search(
                headers,
                false,
                query,
                SearchService.CLIENTS
            )
        }.also { outcome ->
            when (outcome) {
                is Success -> {
                    outcome.data?.forEach {
                        it.handleSearch(headers)
                    }
                }
                else -> {}
            }
        }
    }

    private suspend fun Search.handleSearch(headers: Map<String, String>) {
        when (entityType) {
            ENTITY_CLIENT ->
                respond {
                    service.client(headers, entityId)
                }.apply {
                    clientOutcome()
                }
            else -> {}
        }
    }

    private fun Outcome<Client>.clientOutcome() {
        when (this) {
            is Success -> {
                data?.let { client ->
                    searchedClientList += client
                    _searchedClients.value =
                        Success(PagingData.from(searchedClientList))
                }
            }
            else -> {}
        }
    }

    suspend fun transactions(accountId: Int) {
        respond {
            service.transactions(
                headers(),
                accountId
            )
        }.also {
            _transactions.value = it
        }
    }

    private suspend fun headers() = prefRepo.authKey().headers
}