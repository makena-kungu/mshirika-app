package co.ke.mshirika.mshirika_app.data_layer.repositories.loans

import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateGuarantor
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Repayment
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.*
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.GuarantorsTemplateWithClient
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import kotlinx.coroutines.flow.Flow

interface LoansRepo {
    val loans: Flow<PagingData<ConservativeLoanAccount>>

    suspend fun createGuarantor(
        loanId: Int,
        createGuarantor: CreateGuarantor
    ): Outcome<CreateGuarantorResponse>

    suspend fun detailedLoanAccount(
        loanId: Int
    ): DetailedLoanAccount?

    suspend fun getLoanTemplate(
        clientId: Int
    ): NewLoanTemplate?

    suspend fun getLoanTemplate(
        clientId: Int,
        productId: Int
    ): NewLoanTemplate2?

    suspend fun guarantorsTemplate(
        loanId: Int
    ): LoanWithGuarantors?

    suspend fun guarantorsTemplate(
        clientId: Int,
        loanId: Int
    ): GuarantorsTemplateWithClient?
    suspend fun newLoan(
        newLoan: NewLoan
    ): Outcome<CreateLoan>
    suspend fun repay(loanId: Int, repayment: Repayment): RepaymentSuccessful?
    suspend fun repaymentSchedule(loanId: Int): LoanRepaymentSchedule?
    suspend fun repaymentTypes(): RepaymentResponse?
    suspend fun headers(): Map<String, String>
}