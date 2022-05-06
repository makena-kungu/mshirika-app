package co.ke.mshirika.mshirika_app.data_layer.remote.models.request


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class NewLoan(
    val productId: Int,
    val loanOfficerId: Int,
    val loanPurposeId: Int,
    val fundId: String,
    val submittedOnDate: String,
    val expectedDisbursementDate: String,
    val externalId: String,
    val linkAccountId: Int,
    val createStandingInstructionAtDisbursement: String,
    val principal: Int,
    val loanTermFrequency: Int,
    val loanTermFrequencyType: Int,
    val numberOfRepayments: Int,
    val repaymentEvery: Int,
    val repaymentFrequencyType: Int,
    val repaymentFrequencyNthDayType: String,
    val repaymentFrequencyDayOfWeekType: String,
    val repaymentsStartingFromDate: String? = null,
    val interestChargedFromDate: String? = null,
    val interestRatePerPeriod: Int,
    val interestType: Int,
    val isEqualAmortization: Boolean = false,
    val amortizationType: Int,
    val interestCalculationPeriodType: Int,
    val allowPartialPeriodInterestCalcualtion: Boolean,
    val transactionProcessingStrategyId: Int,
    val graceOnArrearsAgeing: Int,
    val loanIdToClose: String,
    val isTopup: String,
    val recalculationCompoundingFrequencyDate: String,
    val charges: List<Charge>,
    val collateral: List<String> = emptyList(),
    val clientId: Int,
    val dateFormat: String,
    val locale: String,
    val loanType: String
) : Parcelable {
    @Keep
    @Parcelize
    data class Charge(
        val chargeId: Int,
        val amount: Int
    ) : Parcelable
}