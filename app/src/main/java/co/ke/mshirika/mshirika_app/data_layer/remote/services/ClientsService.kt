package co.ke.mshirika.mshirika_app.data_layer.remote.services

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.ClientSms
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.DepositShares
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.TransferFunds
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.TransferFundsTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.TransferFundsTemplateWithToClients
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.TransferFundsTemplateWithToClientsAccounts
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
        @Body client: CreateClient
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
        @Query("command") command: String = "deposit",
        @Body deposit: DepositShares
    ): Response<DepositResponse>

    @POST(EndPoint.SMS)
    suspend fun sendSms(
        @HeaderMap headers: Map<String, String>,
        @Body clientSms: ClientSms
    ): Response<ClientSmsResponse>

    @GET(EndPoint.CLIENTS_TEMPLATE)
    suspend fun template(
        @HeaderMap headers: Map<String, String>
    ): Response<ClientTemplate>

    @GET(EndPoint.CLIENT_PAYMENT)
    suspend fun templateClientPayment(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int
    ): Response<ClientPaymentTemplate>

    @GET(EndPoint.ACCOUNTS_TRANSFER_TEMPLATE)
    suspend fun transferFundsTemplate(
        @HeaderMap headers: Map<String, String>,
        @Query("fromAccountId") fromAccountId: Int,
        @Query("fromAccountType") fromAccountType: Int = 2
    ): Response<TransferFundsTemplate>

    @GET(EndPoint.ACCOUNTS_TRANSFER_TEMPLATE)
    suspend fun transferFundsTemplateWithToClients(
        @HeaderMap headers: Map<String, String>,
        @Query("fromAccountId") fromAccountId: Int,
        @Query("toOfficeId") toOfficeId: Int,
        @Query("fromAccountType") fromAccountType: Int = 2
    ): Response<TransferFundsTemplateWithToClients>

    @GET(EndPoint.ACCOUNTS_TRANSFER_TEMPLATE)
    suspend fun transferFundsTemplateWithAccounts(
        @HeaderMap headers: Map<String, String>,
        @Query("fromAccountId") fromAccountId: Int,
        @Query("toOfficeId") toOfficeId: Int,
        @Query("toAccountType") toAccountType: Int = 2,
        @Query("fromAccountType") fromAccountType: Int = 2
    ): Response<TransferFundsTemplateWithToClientsAccounts>

    @GET("${EndPoint.SAVINGS_ACCOUNTS}/{accountId}")
    suspend fun transactions(
        @HeaderMap headers: Map<String, String>,
        @Path("accountId") accountId: Int,
        @Query("associations") associations: String = "all",
        @Query("checkOfficeHierarchy") boolean: Boolean = false
    ): Response<TransactionResponse>

    @POST(EndPoint.ACCOUNTS_TRANSFER)
    suspend fun transferFunds(
        @HeaderMap headers: Map<String, String>,
        @Body transfer: TransferFunds
    ): Response<co.ke.mshirika.mshirika_app.data_layer.remote.models.response.TransferFunds>
}