package co.ke.mshirika.mshirika_app.data_layer.remote.services

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Repayment
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint.Paths.LOAN_ID
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Feedback
import retrofit2.Response
import retrofit2.http.*

interface LoansService {

    @GET(EndPoint.LOANS)
    suspend fun loan(
        @HeaderMap headers: Map<String, String>,
        @Query("offset") page: Int,
        @Query("limit") perPage: Int
    ): Response<Feedback<LoanAccount>>

    @GET(EndPoint.LOANS_PATH)
    suspend fun loan(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loanId: Int,
        @Query("associations") associations: String = "all",
        @Query("exclude") exclude: Array<String> = arrayOf("guarantors,futureSchedule")
    ): Response<LoanAccount>

    @POST(EndPoint.LOANS)
    suspend fun newLoan(
        @HeaderMap headers: Map<String, String>,
        newLoan: NewLoan
    )

    @GET(EndPoint.PAYMENT_TYPES)
    suspend fun repaymentType(
        @HeaderMap headers: Map<String, String>,
    ): Response<RepaymentResponse>

    @GET(EndPoint.LOANS_PATH)
    suspend fun loanRepaymentSchedule(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loadId: String,
        @Query("associations") association: String = "repaymentSchedule"
    ): Response<LoanRepaymentSchedule>

    @POST(EndPoint.LOANS_TRANSACTIONS)
    suspend fun repay(
        @HeaderMap headers: Map<String, String>,
        @Path(LOAN_ID) loanId: Int,
        @Query("command") command: String = "repayment",
        repayment: Repayment
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
}