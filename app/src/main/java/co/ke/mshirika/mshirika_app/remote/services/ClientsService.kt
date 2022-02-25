package co.ke.mshirika.mshirika_app.remote.services

import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.remote.EndPoint
import co.ke.mshirika.mshirika_app.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.remote.response.ClientResponse
import retrofit2.Response
import retrofit2.http.*


interface ClientsService {

    @GET("${EndPoint.CLIENTS}/{clientId}/accounts")
    suspend fun accounts(
        @HeaderMap headers: Map<String, String>,
        @Path("clientId") clientId: Int
    ): Response<AccountsResponse>

    @Headers("Fineract-Platform-TenantId: default")
    @GET(EndPoint.CLIENTS)
    suspend fun clients(
        @Header("Authorization") authKey: String,
        @Query("offset") page: Int,
        @Query("limit") perPage: Int
    ): Response<ClientResponse>

    @GET(EndPoint.CLIENTS)
    suspend fun clients(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = true,
        @Query("offset") page: Int,
        @Query("limit") perPage: Int
    ): Response<ClientResponse>

    @GET("${EndPoint.CLIENTS}/{clientId}")
    suspend fun client(
        @HeaderMap headers: Map<String, String>,
        @Path("clientId") clientId: Int
    ): Response<Client>

    @GET("${EndPoint.LOANS}/{loanId}")
    suspend fun loanRepaymentSchedule(
        @HeaderMap headers: Map<String, String>,
        @Path("loanId") loadId: String,
        @Query("associations") association: String = "repaymentSchedule"
    ): Response<LoanRepaymentSchedule>
}