package co.ke.mshirika.mshirika_app.data_layer.repositories

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
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.empty
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLoansRepo : LoansRepo {
    override val loans: Flow<PagingData<ConservativeLoanAccount>>
        get() = flowOf(PagingData.empty())

    override suspend fun createGuarantor(
        loanId: Int,
        createGuarantor: CreateGuarantor
    ): Outcome<CreateGuarantorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun detailedLoanAccount(loanId: Int): DetailedLoanAccount? {
        TODO("Not yet implemented")
    }

    override suspend fun getLoanTemplate(clientId: Int): NewLoanTemplate? {
        TODO("Not yet implemented")
    }

    override suspend fun getLoanTemplate(clientId: Int, productId: Int): NewLoanTemplate2? {
        TODO("Not yet implemented")
    }

    override suspend fun guarantorsTemplate(loanId: Int): LoanWithGuarantors? {
        TODO("Not yet implemented")
    }

    override suspend fun guarantorsTemplate(
        clientId: Int,
        loanId: Int
    ): GuarantorsTemplateWithClient? {
        TODO("Not yet implemented")
    }

    override suspend fun newLoan(newLoan: NewLoan): Outcome<CreateLoan> {
        val matches = Util.match(newLoan)
        return if (matches) Outcome.Success()
        else empty()
    }

    override suspend fun repaymentSchedule(loanId: Int): LoanRepaymentSchedule? {
        TODO("Not yet implemented")
    }

    override suspend fun repaymentTypes(): RepaymentResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun repay(loanId: Int, repayment: Repayment): RepaymentSuccessful? {
        TODO("Not yet implemented")
    }

    override suspend fun headers(): Map<String, String> {
        TODO("Not yet implemented")
    }
}