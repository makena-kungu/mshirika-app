package co.ke.mshirika.mshirika_app.remote.services

import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.remote.response.LoansResponse
import co.ke.mshirika.mshirika_app.remote.utils.EndPoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

interface LoansService {

    @GET(EndPoint.LOANS)
    suspend fun loan(
        @HeaderMap headers: Map<String, String>,
        @Query("offset") page: Int,
        @Query("limit") perPage: Int
    ): Response<LoansResponse>

    @GET("${EndPoint.LOANS}/{loanId}")
    suspend fun loan(
        @HeaderMap headers: Map<String, String>,
        @Path("loanId") loanId: Int,
        @Query("associations") associations: String = "all",
        @Query("exclude") exclude: Array<String> = arrayOf("guarantors,futureSchedule")
    ): Response<LoanAccount>

    @GET("${EndPoint.LOANS}/{loanId}")
    suspend fun loanRepaymentSchedule(
        @HeaderMap headers: Map<String, String>,
        @Path("loanId") loadId: String,
        @Query("associations") association: String = "repaymentSchedule"
    ): Response<LoanRepaymentSchedule>
}