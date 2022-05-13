package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.CreateLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.NewLoanDetailsFragment.Companion.Details
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.NewLoanTermsFragment.Companion.Terms
import javax.inject.Inject

class CreateLoanUseCase @Inject constructor(
    private val repo: LoansRepo
) {

    suspend operator fun invoke(
        savingsAccountId:Int,
        details: Details,
        terms: Terms,
        template2: NewLoanTemplate2,
        charges: List<NewLoan.Charge>
    ): Outcome<CreateLoan> {


        val newLoan = NewLoan(
            productId = template2.loanProductId,
            loanOfficerId = details.officer,
            loanPurposeId = details.purpose,
            submittedOnDate = details.date,
            expectedDisbursementDate = details.disbursementDate,
            externalId = details.formNumber,
            linkAccountId = savingsAccountId,//link to the default savings account
            createStandingInstructionAtDisbursement = "",
            principal = terms.principal,
            loanTermFrequency = terms.loanTerm,
            loanTermFrequencyType = terms.frequency,
            numberOfRepayments = terms.loanTerm,
            repaymentEvery = terms.repaidEvery,
            repaymentFrequencyType = template2.repaymentFrequencyTypeOptions
                .first { it.value == terms.repaymentFrequency }
                .id,
            interestRatePerPeriod = template2.interestRatePerPeriod.toInt(),
            interestType = template2.interestType.id,
            amortizationType = template2.amortizationType.id,
            interestCalculationPeriodType = template2.interestCalculationPeriodType.id,
            allowPartialPeriodInterestCalcualtion = template2.allowPartialPeriodInterestCalcualtion,
            transactionProcessingStrategyId = template2.transactionProcessingStrategyId,
            graceOnArrearsAgeing = template2.graceOnArrearsAgeing,
            charges = charges,
            clientId = template2.clientId
        )

        return repo.newLoan(newLoan)
    }
}