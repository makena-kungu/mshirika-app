package co.ke.mshirika.mshirika_app.data_layer.repositories

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
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.empty
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLoansRepo : LoansRepo {
    override val loans: Flow<PagingData<ConservativeLoanAccount>>
        get() = flowOf(PagingData.empty())

    override val prestamos: Flow<List<ConservativeLoanAccount>>
        get() = TODO("Not yet implemented")

    override suspend fun createGuarantor(
        loanId: Int,
        createGuarantor: CreateGuarantor
    ): Outcome<CreateGuarantorResponse> {
        return empty()
    }

    override suspend fun detailedLoanAccount(loanId: Int): DetailedLoanAccount? {
        return null
    }

    override suspend fun getLoanTemplate(clientId: Int): NewLoanTemplate? {
        return null
    }

    override suspend fun getLoanTemplate(clientId: Int, productId: Int): NewLoanTemplate2? {
        return null
    }

    override suspend fun guarantorsTemplate(loanId: Int): LoanWithGuarantors? {
        return null
    }

    override suspend fun guarantorsTemplate(
        clientId: Int,
        loanId: Int
    ): GuarantorsTemplateWithClient? {
        return null
    }

    override suspend fun newLoan(newLoan: NewLoan): Outcome<CreateLoan> {
        val matches = Util.match(newLoan)
        return if (matches) Outcome.Success()
        else empty()
    }

    override suspend fun repaymentSchedule(loanId: Int): LoanRepaymentSchedule? {
        return null
    }

    override suspend fun repaymentTypes(): RepaymentResponse? {
        return null
    }

    override suspend fun repay(loanId: Int, repayment: PaymentTransaction): RepaymentSuccessful? {
        return null
    }

    override suspend fun headers(): Map<String, String> {
        return emptyMap()
    }
}