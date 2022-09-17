package co.ke.mshirika.mshirika_app.data_layer.repositories.clients

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.ClientDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddBeneficiary
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddNok
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.DetailedLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.EditClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.ClientCreationResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Feedback
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.empty
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.ClientsPagingSource
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.PagingSourceUtil.pagingConfig
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.ClientsRemoteMediator
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
class ClientsRepoImpl @Inject constructor(
    private val service: ClientsService,
    private val loansService: LoansService,
    private val searchService: SearchService,
    private val store: PreferencesStoreRepository,
    private val remoteMediator: ClientsRemoteMediator,
    private val cacheDao: CacheDao,
    private val clientDao: ClientDao
) : ClientsRepo {
    private var call: Call<ClientCreationResponse>? = null

    private val _created = MutableStateFlow<Outcome<ClientCreationResponse>>(empty())
    private val _searchedClients = MutableStateFlow<Outcome<PagingData<Cliente>>>(empty())
    private val query: MutableStateFlow<String?> = MutableStateFlow(null)
    override val created: StateFlow<Outcome<ClientCreationResponse>>
        get() = _created.asStateFlow()

    override val clients: Flow<PagingData<Cliente>> = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { cacheDao.clients() },
        remoteMediator = remoteMediator
    ).flow

    override val clientes: Flow<PagingData<Cliente>> = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { clientDao.clients() }
    ).flow


    @OptIn(ExperimentalCoroutinesApi::class)
    override val searchedClients: Flow<PagingData<Cliente>> = query.flatMapLatest { query ->
        Pager(
            config = pagingConfig(),
            pagingSourceFactory = {
                ClientsPagingSource(
                    store = store,
                    service = searchService,
                    query = query
                )
            }
        ).flow
    }

    override suspend fun accounts(clientId: Int): Outcome<AccountsResponse> =
        withContext(IO) {
            respond {
                service.accounts(
                    headers = store.headers(),
                    clientId = clientId
                )
            }
        }

    override suspend fun addNok(
        clientId: Int,
        addNok: AddNok
    ): CreateNok? = withContext(IO) {
        respondWithSuccess {
            service.addNok(
                store.headers(),
                clientId,
                addNok
            )
        }
    }

    override suspend fun addBeneficiary(
        clientId: Int,
        beneficiary: AddBeneficiary
    ): CreateBeneficiary? = withContext(IO) {
        respondWithSuccess {
            service.addBeneficiary(
                headers = store.headers(),
                clientId = clientId,
                beneficiary = beneficiary
            )
        }
    }

    override suspend fun beneficiaries(clientId: Int) = withContext(IO) {
        respondWithSuccess {
            service.beneficiario(
                headers = store.headers(),
                clientId = clientId
            )
        }
    }

    override suspend fun family(clientId: Int): Family? = withContext(IO) {
        respondWithSuccess {
            service.familia(
                headers = store.headers(),
                clientId = clientId
            )
        }
    }

    override suspend fun nok(clientId: Int) = withContext(IO) {
        respondWithSuccess {
            service.nextOfKin(
                headers = store.headers(),
                clientId = clientId
            )
        }
    }

    override suspend fun loans(loanId: Int): Outcome<ConservativeLoanAccount> =
        withContext(IO) {
            Log.d(TAG, "loans: $loanId")
            respond {
                loansService.loan(
                    store.headers(),
                    loanId
                )
            }
        }

    override suspend fun search(query: String): Feedback<Cliente>? = withContext(IO) {
        Log.d(TAG, "search: invoked")
        _searchedClients.value = Outcome.Loading()
        val headers = store.headers()
        respondWithSuccess {
            searchService.searchClient(
                headers = headers,
                sqlSearch = SearchService.clients(query)
            )
        }
    }

    override suspend fun client(clientId: Int): DetailedClient? {
        return respondWithSuccess {
            service.client(store.headers(), clientId)
        }
    }

    override suspend fun createClient(client: CreateClient) = withContext(IO) {
        respondWithSuccess {
            service.create(store.headers(), client)
        }
    }

    override suspend fun editClient(client: CreateClient) = withContext(IO) {
        respondWithSuccess {
            service.edit(
                headers = store.headers(),
                client = client
            )
        }
    }

    override suspend fun editClientTemplate(clientId: Int): EditClientTemplate? {
        return respondWithSuccess {
            service.editTemplate(
                store.headers(),
                clientId
            )
        }
    }

    override fun cancel() {
        call?.cancel()
    }

    override suspend fun detailedLoans(loanId: Int): DetailedLoanAccount? =
        withContext(IO) {
            respondWithSuccess {
                loansService.detailedLoan(
                    headers = store.headers(),
                    loanId = loanId
                )
            }
        }

    override suspend fun transactions(accountId: Int): Outcome<TransactionResponse> =
        withContext(IO) {
            respond {
                service.transactions(
                    store.headers(),
                    accountId
                )
            }
        }

    override suspend fun template(): ClientTemplate? = withContext(IO) {
        respondWithSuccess {
            service.template(store.headers())
        }
    }

    companion object {
        private const val TAG = "ClientsRepoImpl"
    }
}