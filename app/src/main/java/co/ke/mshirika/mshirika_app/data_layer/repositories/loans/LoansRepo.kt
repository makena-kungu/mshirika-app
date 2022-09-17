package co.ke.mshirika.mshirika_app.data_layer.repositories.loans

import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateGuarantor
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.guarantors.GuarantorsTemplateWithClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import kotlinx.coroutines.flow.Flow

interface LoansRepo {
    val loans: Flow<PagingData<ConservativeLoanAccount>>
    val prestamos:Flow<List<ConservativeLoanAccount>>

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
    suspend fun repay(loanId: Int, repayment: PaymentTransaction): RepaymentSuccessful?
    suspend fun repaymentSchedule(loanId: Int): LoanRepaymentSchedule?
    suspend fun repaymentTypes(): RepaymentResponse?
    suspend fun headers(): Map<String, String>
}