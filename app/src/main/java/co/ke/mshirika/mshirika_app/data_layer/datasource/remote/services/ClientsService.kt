package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.ClientSms
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.TransferFunds
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddBeneficiary
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddNok
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.CreateCharge
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.EditClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplateWithToClients
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplateWithToClientsAccounts
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.CHARGE_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.CLIENT_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.SAVINGS_ACCOUNT_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Feedback
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.TransferFunds as TransferFunds1


interface ClientsService {

    @GET(EndPoint.DATATABLES_BENEFICIARY)
    suspend fun beneficiario(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int,
        @Query("genericResultSet") genericResultSet: Boolean = true
    ): Response<Beneficiary>

    @GET(EndPoint.DATATABLES_NEXT_OF_KIN)
    suspend fun nextOfKin(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int,
        @Query("genericResultSet") genericResultSet: Boolean = true
    ): Response<NextOfKin>

    @GET(EndPoint.CLIENTS_FAMILY)
    suspend fun familia(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int
    ): Response<Family>

    @GET(EndPoint.CLIENT_ACCOUNTS)
    suspend fun accounts(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int
    ): Response<AccountsResponse>

    @GET(EndPoint.CLIENT_ACCOUNTS_DETAILED)
    suspend fun account(
        @HeaderMap headers: Map<String, String>,
        @Path(SAVINGS_ACCOUNT_ID) savingsAccountId: Int
    ): Response<DetailedSavingsAccount>

    @GET(EndPoint.CLIENTS)
    suspend fun clients(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = false
    ): Response<Feedback<Cliente>>

    @GET(EndPoint.CLIENTS)
    suspend fun clients(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = false,
        @Query("called by") calledBy:String? = null
    ): Response<Feedback<Cliente>>

    @GET(EndPoint.CLIENT)
    suspend fun client(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int
    ): Response<DetailedClient>

    @POST(EndPoint.CLIENTS)
    suspend fun create(
        @HeaderMap headers: Map<String, String>,
        @Body client: CreateClient
    ): Response<ClientCreationResponse>

    @PUT(EndPoint.CLIENTS)
    suspend fun edit(
        @HeaderMap headers: Map<String, String>,
        @Body client: CreateClient
    ): Response<EditClientResponse>

    @GET(EndPoint.CLIENTS_EDIT_TEMPLATE)
    suspend fun editTemplate(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int
    ): Response<EditClientTemplate>

    @POST(EndPoint.CLIENT_DEPOSIT)
    suspend fun deposit(
        @HeaderMap headers: Map<String, String>,
        @Path(SAVINGS_ACCOUNT_ID) savingsAccountId: Int,
        @Query("command") command: String = "deposit",
        @Body deposit: PaymentTransaction
    ): Response<DepositResponse>

    @POST(EndPoint.CLIENT_DEPOSIT_CHARGE)
    suspend fun charge(
        @HeaderMap headers: Map<String, String>,
        @Path(SAVINGS_ACCOUNT_ID) savingsAccountId: Int,
        @Path(CHARGE_ID) chargeId: Int,
        @Body charge: PaymentTransaction
    )

    @POST(EndPoint.SMS)
    suspend fun sendSms(
        @HeaderMap headers: Map<String, String>,
        @Body clientSms: ClientSms
    ): Response<ClientSmsResponse>

    @GET(EndPoint.CLIENTS_TEMPLATE)
    suspend fun template(
        @HeaderMap headers: Map<String, String>
    ): Response<ClientTemplate>

    @GET(EndPoint.CHARGE_TEMPLATE)
    suspend fun chargeTemplate(
        @HeaderMap headers: Map<String, String>,
        @Path(SAVINGS_ACCOUNT_ID) savingsAccountId: Int
    ): Response<ChargesTemplate>

    @GET(EndPoint.CHARGE_TEMPLATE2)
    suspend fun chargeTemplate2(
        @HeaderMap headers: Map<String, String>,
        @Path(CHARGE_ID) chargeId: Int
    ): Response<ChargesTemplate2>

    @POST(EndPoint.CREATE_CHARGES)
    suspend fun charge(
        @HeaderMap headers: Map<String, String>,
        @Path(SAVINGS_ACCOUNT_ID) savingsAccountId: Int,
        @Body createCharge: CreateCharge
    )

    @GET("savingsaccounts/$SAVINGS_ACCOUNT_ID/charges?chargeStatus=active")
    suspend fun charges(
        @HeaderMap headers: Map<String, String>,
        @Path(SAVINGS_ACCOUNT_ID) savingsAccountId: Int
    ): Response<ChargesResponse>

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
    ): Response<TransferFunds1>

    @POST(EndPoint.DATATABLES_BENEFICIARY)
    suspend fun addBeneficiary(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int,
        @Body beneficiary: AddBeneficiary
    ): Response<CreateBeneficiary>

    @POST(EndPoint.DATATABLES_NEXT_OF_KIN)
    suspend fun addNok(
        @HeaderMap headers: Map<String, String>,
        @Path(CLIENT_ID) clientId: Int,
        @Body addNok: AddNok
    ): Response<CreateNok>

    @GET("${EndPoint.CLIENTS}/{clientId}/images?maxHeight=10,maxWidth=10")
    suspend fun image(
        @HeaderMap map: Map<String, String>,
        @Path("clientId") clientId: Int
    ): Response<ResponseBody>
}