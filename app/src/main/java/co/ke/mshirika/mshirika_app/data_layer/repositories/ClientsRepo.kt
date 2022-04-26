package co.ke.mshirika.mshirika_app.data_layer.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.CreateClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Search
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Search.Companion.ENTITY_CLIENT
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.ClientsPagingSource
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.data_layer.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.response.ClientCreationResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome.Loading
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respondWithCallback
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.empty
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

class ClientsRepo @Inject constructor(
    private val service: ClientsService,
    private val loansService: LoansService,
    private val searchService: SearchService,
    private val prefRepo: PreferencesStoreRepository
) {
    private val searchedClientList = mutableListOf<Client>()
    private val loanAccounts = mutableListOf<LoanAccount>()
    private var call: Call<ClientCreationResponse>? = null

    private val _accounts = MutableStateFlow<Outcome<AccountsResponse>>(empty())
    private val _created = MutableStateFlow<Outcome<ClientCreationResponse>>(empty())
    private val _loans = MutableStateFlow<List<LoanAccount>>(emptyList())
    private val _searchedClients = MutableStateFlow<Outcome<PagingData<Client>>>(empty())
    private val _transactions = MutableStateFlow<Outcome<TransactionResponse>>(empty())
    private val _template = MutableStateFlow<Outcome<CreateClientTemplate>>(empty())

    val accounts
        get() = _accounts
    val created
        get() = _created.asStateFlow()
    val loans
        get() = _loans
    val searchedClients
        get() = _searchedClients.asStateFlow()
    val transactions
        get() = _transactions.asStateFlow()
    val template
        get() = _template.asStateFlow()

    val template1 = flow {
        withContext(IO) {
            respond { service.template(headers()) }.also { emit(it) }
        }
    }

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

    suspend fun createClient(client: CreateClient) {
        _created.value = respondWithCallback {
            service.createWithCall(headers(), client).also { call = it }
        }
    }

    fun cancel() {
        call?.cancel()
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

    suspend fun template() =
        respond {
            service.template(headers())
        }.let {
            _template.value = it
        }

    private suspend fun headers() = prefRepo.authKey().headers
}