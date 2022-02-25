package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.pagingSource.ClientsPS
import co.ke.mshirika.mshirika_app.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.remote.response.ClientResponse
import co.ke.mshirika.mshirika_app.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.utility.network.Outcome
import co.ke.mshirika.mshirika_app.utility.network.Resource
import co.ke.mshirika.mshirika_app.utility.network.outcome
import co.ke.mshirika.mshirika_app.utility.network.resource
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientsRepo @Inject constructor(
    private val clientsService: ClientsService,
    private val searchService: SearchService
) {
    private val _searched = MutableSharedFlow<PagingData<Client>>()
    private val _accounts = MutableStateFlow<Resource<AccountsResponse>>(Resource.empty())

    val accounts = _accounts.asSharedFlow()
    val searched: Flow<PagingData<Client>> get() = _searched.asSharedFlow()


    fun clients(authKey: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 60,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                ClientsPS(
                    authKey,
                    clientsService
                )
            }
        ).flow

    suspend fun loadClientAccounts(client: Client, headers: Map<String, String>) =
        loadClientAccounts(client.id, headers)

    private suspend fun loadClientAccounts(clientId: Int, headers: Map<String, String>) {
        _accounts.value = Resource.loading()

        val result: Resource<AccountsResponse> =
            try {
                clientsService.accounts(headers, clientId).run {
                    var response: AccountsResponse? = null
                    if (isSuccessful) {
                        body()?.let {
                            response = it
                        }
                        Resource.success(response)
                    } else kotlin.runCatching { errorBody()?.string() }
                        .let {
                            it.getOrNull()?.let { msg -> Resource.error(msg) }
                        } ?: Resource.error()

                }
            } catch (e: HttpException) {
                //non-2** status codes
                Resource.error("")
            } catch (e: IOException) {
                //probably no net
                Resource.error("Error Loading Data, Check Your Internet")
            }

        _accounts.value = result
    }

    suspend fun queryRepaymentPeriod(
        loanAccount: LoanAccount,
        headers: Map<String, String>
    ): Outcome<LoanRepaymentSchedule> =
        kotlin.runCatching {
            clientsService.loanRepaymentSchedule(
                headers,
                loanAccount.accountNo
            )
        }.get()

    private fun <T> Result<Response<T>>.get(): Outcome<T> {
        return if (isFailure) exceptionOrNull()!!.outcome()
        else {
            getOrNull()!!.run {
                if (isSuccessful) Outcome.Success(body())
                else kotlin.runCatching {
                    errorBody()?.string()
                }.let {
                    Outcome.Error(it.getOrNull())
                }
            }
        }
    }

    suspend fun search(query: String, headers: Map<String, String>) {
        kotlin.runCatching {
            searchService.search(
                headers,
                false,
                query,
                arrayOf("clients")
            )
        }.apply {

            when {
                isFailure ->
                    exceptionOrNull()!!.resource<ClientResponse>()
                else -> {
                    //query the account and add it to the list
                    searchService.search(
                        headers,
                        false,
                        query,
                        arrayOf("clients")
                    ).run {
                        body().run {
                            val list = mutableListOf<Client>()

                            this?.forEach {
                                if (it.entityType == "CLIENT") {
                                    it.entityAccountNo
                                    //query the account and add it to the list
                                    clientsService.client(headers, it.entityId).body()
                                        ?.let { client ->
                                            list += client
                                        }
                                }
                            }
                            Response.success(ClientResponse(list))
                        }.let {
                            kotlin.runCatching { it }.get()
                        }
                    }.also {
                        it.data?.pageItems?.let { client ->
                            val data: PagingData<Client> = PagingData.from(client)
                            _searched.emit(data)
                        }
                    }
                }
            }


        }

        suspend fun otherway(clientId: Int) {
            val r: Resource<AccountsResponse> =
                kotlin.runCatching { clientsService.accounts(headers, clientId) }
                    .let {
                        if (it.isSuccess) it.getOrNull()!!.run {
                            if (isSuccessful) Resource.success(body())
                            else Resource.error()
                        } else it.exceptionOrNull()!!.resource()
                    }
            _accounts.value = r
        }
    }
}