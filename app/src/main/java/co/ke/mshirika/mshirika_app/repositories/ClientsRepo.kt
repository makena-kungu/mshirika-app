package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.Search
import co.ke.mshirika.mshirika_app.data.response.Search.Companion.ENTITY_CLIENT
import co.ke.mshirika.mshirika_app.pagingSource.ClientsPS
import co.ke.mshirika.mshirika_app.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.remote.response.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.utility.network.Result
import co.ke.mshirika.mshirika_app.utility.network.Result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientsRepo @Inject constructor(
    private val clientsService: ClientsService,
    private val searchService: SearchService
) {
    private val searchedClientList = mutableListOf<Client>()
    private val loanAccounts = mutableListOf<LoanAccount>()

    private val _accounts = MutableStateFlow<Result<AccountsResponse>>(Empty())
    private val _loans = MutableStateFlow<List<LoanAccount>>(emptyList())
    private val _searchedClients = MutableStateFlow<Result<PagingData<Client>>>(Empty())
    private val _transactions = MutableStateFlow<Result<TransactionResponse>>(Empty())

    val accounts
        get() = _accounts
    val loans
        get() = _loans
    val searchedClients
        get() = _searchedClients.asStateFlow()
    val transactions
        get() = _transactions.asStateFlow()


    fun clients(authKey: String) =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                maxSize = 100,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                ClientsPS(
                    authKey,
                    clientsService
                )
            }
        ).flow

    suspend fun accounts(clientId: Int, headers: Map<String, String>) {
        _accounts.value = Loading()

        respond {
            clientsService.accounts(headers, clientId)
        }.also {
            _accounts.value = it
        }
    }

    suspend fun loans(loanId: Int, headers: Map<String, String>) {
        respond {
            clientsService.loans(headers, loanId)
        }.apply {
            if (this is Success) data?.let { loanAccounts += it }
            _loans.value = loanAccounts
        }
    }

    suspend fun search(query: String, headers: Map<String, String>) {
        _searchedClients.value = Loading()

        respond {
            searchService.search(
                headers,
                false,
                query,
                arrayOf("clients")
            )
        }.also { outcome ->
            when (outcome) {
                is Success -> {
                    outcome.data?.parallelStream()?.handleSearch(headers)
                }
                else -> {}
            }
        }
    }

    private suspend fun Stream<Search>.handleSearch(headers: Map<String, String>) {
        forEach {
            when (it.entityType) {
                ENTITY_CLIENT -> {
                    CoroutineScope(IO).launch {
                        respond {
                            clientsService.client(headers, it.entityId)
                        }.apply {
                            clientOutcome()
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun Result<Client>.clientOutcome() {
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

    suspend fun transaction(accountId: Int, headers: Map<String, String>) {
        respond {
            clientsService.transactions(
                headers,
                accountId
            )
        }.also {
            _transactions.value = it
        }
    }
}