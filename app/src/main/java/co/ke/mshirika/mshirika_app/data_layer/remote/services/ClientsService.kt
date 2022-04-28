package co.ke.mshirika.mshirika_app.data_layer.remote.services

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.DepositShares
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.response.*
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint.Paths.CLIENT_ID
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint.Paths.SAVINGS_ACCOUNT_ID
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Feedback
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ClientsService {

    @GET(EndPoint.CLIENT_ACCOUNTS)
    suspend fun accounts(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int
    ): Response<AccountsResponse>

    @GET(EndPoint.CLIENTS)
    suspend fun clients(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = true,
        @Query("offset") page: Int,
        @Query("limit") perPage: Int
    ): Response<Feedback<Client>>

    @GET(EndPoint.CLIENT)
    suspend fun client(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int
    ): Response<Client>

    @POST(EndPoint.CLIENTS)
    suspend fun create(
        @HeaderMap headers: Map<String, String>,
        client: CreateClient
    ): Response<ClientCreationResponse>

    @POST(EndPoint.CLIENTS)
    suspend fun createWithCall(
        @HeaderMap headers: Map<String, String>,
        client: CreateClient
    ): Call<ClientCreationResponse>

    @POST(EndPoint.CLIENT_DEPOSIT)
    suspend fun deposit(
        @HeaderMap headers: Map<String, String>,
        @Path(SAVINGS_ACCOUNT_ID) savingsAccountId: Int,
        deposit: DepositShares,
        @Query("command") command: String = "deposit"
    ): Response<DepositResponse>

    @GET(EndPoint.CLIENTS_TEMPLATE)
    suspend fun template(
        @HeaderMap headers: Map<String, String>
    ): Response<ClientTemplate>

    @GET(EndPoint.CLIENT_PAYMENT)
    suspend fun templateClientPayment(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int
    ): Response<ClientPaymentTemplate>

    @GET("${EndPoint.SAVINGS_ACCOUNTS}/{accountId}")
    suspend fun transactions(
        @HeaderMap headers: Map<String, String>,
        @Path("accountId") accountId: Int,
        @Query("associations") associations: String = "All",
        @Query("checkOfficeHierarchy") boolean: Boolean = false
    ): Response<TransactionResponse>
}