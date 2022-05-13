package co.ke.mshirika.mshirika_app.data_layer.repositories.clients

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.ClientsPagingSource
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.ClientsPagingSourceWithQuery
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Search
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.response.ClientCreationResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Feedback
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.empty
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

class ClientsRepoImpl @Inject constructor(
    private val service: ClientsService,
    private val loansService: LoansService,
    private val searchService: SearchService,
    private val pagingSource: ClientsPagingSource,
    private val store: PreferencesStoreRepository
) :ClientsRepo{
    private val searchedClientList = mutableListOf<Client>()
    private var call: Call<ClientCreationResponse>? = null

    private val _created = MutableStateFlow<Outcome<ClientCreationResponse>>(empty())
    private val _searchedClients = MutableStateFlow<Outcome<PagingData<Client>>>(empty())
    private val query: MutableStateFlow<String?> = MutableStateFlow(null)
    override val created: StateFlow<Outcome<ClientCreationResponse>>
        get() = _created.asStateFlow()
    override val clients: Flow<PagingData<Client>>
        get() = Pager(
            config = Util.pagingConfig(enablePlaceholders = false),
            pagingSourceFactory = { pagingSource }
        ).flow

    @OptIn(ExperimentalCoroutinesApi::class)
    override val searchedClients: Flow<PagingData<Client>> = query.flatMapLatest { query ->
        Pager(
            config = Util.pagingConfig(),
            pagingSourceFactory = {
                ClientsPagingSourceWithQuery(
                    store = store,
                    service = searchService,
                    query = query
                )
            }
        ).flow
    }

    override suspend fun accounts(clientId: Int): Outcome<AccountsResponse> = withContext(Dispatchers.IO) {
        UnpackResponse.respond {
            service.accounts(store.headers(), clientId)
        }
    }

    override suspend fun loans(loanId: Int): Outcome<ConservativeLoanAccount> = withContext(Dispatchers.IO) {
        Log.d(TAG, "loans: $loanId")
        UnpackResponse.respond {
            loansService.loan(
                store.headers(),
                loanId
            )
        }
    }

    override suspend fun search(query: String): Feedback<Client>? = withContext(Dispatchers.IO) {
        Log.d(TAG, "search: invoked")
        _searchedClients.value = Outcome.Loading()
        val headers = store.headers()
        UnpackResponse.respondWithSuccess {
            searchService.searchClient(
                headers = headers,
                sqlSearch = SearchService.clients(query)
            )
        }
        /*respond {
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
        }*/
    }

    private suspend fun Search.handleSearch(headers: Map<String, String>) {
        when (entityType) {
            Search.ENTITY_CLIENT -> withContext(Dispatchers.IO) {
                UnpackResponse.respond {
                    service.client(headers, entityId)
                }.apply {
                    clientOutcome()
                }
            }
            else -> {}
        }
    }

    private fun Outcome<Client>.clientOutcome() {
        when (this) {
            is Outcome.Success -> {
                data?.let { client ->
                    searchedClientList += client
                    _searchedClients.value =
                        Outcome.Success(PagingData.from(searchedClientList))
                }
            }
            else -> {}
        }
    }

    override suspend fun createClient(client: CreateClient) = withContext(Dispatchers.IO) {
            UnpackResponse.respondWithSuccess {
                service.create(store.headers(), client)
            }
        }

    override fun cancel() {
        call?.cancel()
    }

    override suspend fun transactions(accountId: Int): Outcome<TransactionResponse> =
        withContext(Dispatchers.IO) {
            UnpackResponse.respond {
                service.transactions(
                    store.headers(),
                    accountId
                )
            }
        }

    override suspend fun template(): ClientTemplate? = withContext(Dispatchers.IO) {
        UnpackResponse.respondWithSuccess {
            service.template(store.headers())
        }
    }

    companion object {
        private const val TAG = "ClientsRepoImpl"
    }
}