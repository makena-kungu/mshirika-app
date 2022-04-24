package co.ke.mshirika.mshirika_app.remote.services

import co.ke.mshirika.mshirika_app.data.request.CreateClient
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.CreateClientTemplate
import co.ke.mshirika.mshirika_app.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.remote.response.ClientCreationResponse
import co.ke.mshirika.mshirika_app.remote.response.ClientResponse
import co.ke.mshirika.mshirika_app.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.remote.utils.EndPoint.Paths.CLIENT_ID
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
    ): Response<ClientResponse>

    @GET(EndPoint.CLIENT)
    suspend fun client(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int
    ): Response<Client>

    @POST(EndPoint.CLIENTS)
    suspend fun create(
        @HeaderMap headers: Map<String, String>,
        client: CreateClient
    ):Response<ClientCreationResponse>

    @POST(EndPoint.CLIENTS)
    suspend fun createWithCall(
        @HeaderMap headers: Map<String, String>,
        client: CreateClient
    ): Call<ClientCreationResponse>

    @GET(EndPoint.CLIENTS_TEMPLATE)
    suspend fun template(
        @HeaderMap headers: Map<String, String>
    ): Response<CreateClientTemplate>

    @GET("${EndPoint.SAVINGS_ACCOUNTS}/{accountId}")
    suspend fun transactions(
        @HeaderMap headers: Map<String, String>,
        @Path("accountId") accountId: Int,
        @Query("associations") associations: String = "All",
        @Query("checkOfficeHierarchy") boolean: Boolean = false
    ): Response<TransactionResponse>
}