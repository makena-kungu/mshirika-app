package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateGuarantor
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.guarantors.GuarantorsTemplateWithClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.LOAN_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Feedback
import retrofit2.Response
import retrofit2.http.*

interface LoansService {

    @GET(EndPoint.LOANS)
    suspend fun loans(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = false
    ): Response<Feedback<ConservativeLoanAccount>>

    @GET(EndPoint.LOANS_PATH)
    suspend fun loan(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loanId: Int
    ): Response<ConservativeLoanAccount>

    @GET(EndPoint.LOANS_PATH)
    suspend fun detailedLoan(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loanId: Int,
        @Query("associations") associations: String = "all",
        @Query("exclude") exclude: Array<String> = arrayOf("guarantors,futureSchedule")
    ): Response<DetailedLoanAccount>


    @POST(EndPoint.LOANS)
    suspend fun newLoan(
        @HeaderMap headers: Map<String, String>,
        @Body newLoan: NewLoan
    ): Response<CreateLoan>

    @GET(EndPoint.PAYMENT_TYPES)
    suspend fun repaymentType(
        @HeaderMap headers: Map<String, String>,
    ): Response<RepaymentResponse>

    @GET(EndPoint.LOANS_PATH)
    suspend fun loanRepaymentSchedule(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loadId: Int,
        @Query("associations") association: String = "repaymentSchedule"
    ): Response<LoanRepaymentSchedule>

    @POST(EndPoint.LOANS_TRANSACTIONS)
    suspend fun repay(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loanId: Int,
        @Query("command") command: String = "repayment",
        @Body repayment: PaymentTransaction
    ): Response<RepaymentSuccessful>

    @GET(EndPoint.LOANS_TEMPLATE)
    suspend fun template1(
        @HeaderMap headers: Map<String, String>,
        @Query("activeOnly") activeOnly: Boolean = true,
        @Query("clientId") clientId: Int,
        @Query("staffInSelectedOfficeOnly") staffInSelectedOfficeOnly: Boolean = true,
        @Query("templateType") templateType: String = "individual",
    ): Response<NewLoanTemplate>

    @GET(EndPoint.LOANS_TEMPLATE)
    suspend fun template2(
        @HeaderMap headers: Map<String, String>,
        @Query("activeOnly") activeOnly: Boolean = true,
        @Query("clientId") clientId: Int,
        @Query("staffInSelectedOfficeOnly") staffInSelectedOfficeOnly: Boolean = true,
        @Query("templateType") templateType: String = "individual",
        @Query("productId") productId: Int
    ): Response<NewLoanTemplate2>

    @POST(EndPoint.LOANS_GUARANTORS)
    suspend fun createGuarantor(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loanId: Int,
        @Body createGuarantor: CreateGuarantor
    ): Response<CreateGuarantorResponse>

    @GET(EndPoint.LOANS_GUARANTORS_TEMPLATE)
    suspend fun getGuarantorsTemplate(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loanId: Int,
    ): Response<LoanWithGuarantors>

    @GET(EndPoint.LOANS_GUARANTORS_TEMPLATE)
    suspend fun getGuarantorsTemplate(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loanId: Int,
        @Query("clientId") clientId: Int
    ): Response<GuarantorsTemplateWithClient>
}