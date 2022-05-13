package co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ConservativeLoanAccount(
    val id: Int,
    val accountNo: String,
    val status: Status,
    val clientId: Int,
    val clientAccountNo: String,
    val clientName: String,
    val clientOfficeId: Int,
    val loanProductId: Int,
    val loanProductName: String,
    val loanProductDescription: String,
    val isLoanProductLinkedToFloatingRate: Boolean,
    val loanOfficerId: Int,
    val loanOfficerName: String,
    val loanType: LoanType,
    val currency: Currency,
    val principal: Double,
    val approvedPrincipal: Double,
    val proposedPrincipal: Double,
    val termFrequency: Int,
    val termPeriodFrequencyType: TermPeriodFrequencyType,
    val numberOfRepayments: Int,
    val repaymentEvery: Int,
    val repaymentFrequencyType: RepaymentFrequencyType,
    val interestRatePerPeriod: Double,
    val interestRateFrequencyType: InterestRateFrequencyType,
    val annualInterestRate: Double,
    val isFloatingInterestRate: Boolean,
    val amortizationType: AmortizationType,
    val interestType: InterestType,
    val interestCalculationPeriodType: InterestCalculationPeriodType,
    val allowPartialPeriodInterestCalcualtion: Boolean,
    val transactionProcessingStrategyId: Int,
    val transactionProcessingStrategyName: String,
    val graceOnArrearsAgeing: Int,
    val interestChargedFromDate: List<Int>,
    val expectedFirstRepaymentOnDate: List<Int>,
    val syncDisbursementWithMeeting: Boolean,
    val timeline: Timeline,
    val summary: Summary,
    val feeChargesAtDisbursementCharged: Double,
    val loanProductCounter: Int,
    val multiDisburseLoan: Boolean,
    val canDefineInstallmentAmount: Boolean,
    val canDisburse: Boolean,
    val canUseForTopup: Boolean,
    val isTopup: Boolean,
    val loanIdToClose: Int,
    val inArrears: Boolean,
    val isNPA: Boolean,
    //val overdueCharges: List<Any>,
    val daysInMonthType: DaysInMonthType,
    val daysInYearType: DaysInYearType,
    val isInterestRecalculationEnabled: Boolean,
    val interestRecalculationData: InterestRecalculationData,
    val createStandingInstructionAtDisbursement: Boolean,
    val paidInAdvance: PaidInAdvance,
    val isVariableInstallmentsAllowed: Boolean,
    val minimumGap: Int,
    val maximumGap: Int,
    val isEqualAmortization: Boolean,
    val isRatesEnabled: Boolean
) : Respondent {
    @Keep
    @Parcelize
    data class Status(
        val id: Int,
        val code: String,
        val value: String,
        val pendingApproval: Boolean,
        val waitingForDisbursal: Boolean,
        val active: Boolean,
        val closedObligationsMet: Boolean,
        val closedWrittenOff: Boolean,
        val closedRescheduled: Boolean,
        val closed: Boolean,
        val overpaid: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class LoanType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class Currency(
        val code: String,
        val name: String,
        val decimalPlaces: Int,
        val inMultiplesOf: Int,
        val displaySymbol: String,
        val nameCode: String,
        val displayLabel: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class TermPeriodFrequencyType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class RepaymentFrequencyType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestRateFrequencyType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class AmortizationType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestCalculationPeriodType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class Timeline(
        val submittedOnDate: List<Int>,
        val submittedByUsername: String,
        val submittedByFirstname: String,
        val submittedByLastname: String,
        val approvedOnDate: List<Int>,
        val approvedByUsername: String,
        val approvedByFirstname: String,
        val approvedByLastname: String,
        val expectedDisbursementDate: List<Int>,
        val actualDisbursementDate: List<Int>,
        val disbursedByUsername: String,
        val disbursedByFirstname: String,
        val disbursedByLastname: String,
        val expectedMaturityDate: List<Int>
    ) : Parcelable

    @Keep
    @Parcelize
    data class Summary(
        val currency: Currency,
        val principalDisbursed: Double,
        val principalPaid: Double,
        val principalWrittenOff: Double,
        val principalOutstanding: Double,
        val principalOverdue: Int,
        val interestCharged: Double,
        val interestPaid: Double,
        val interestWaived: Double,
        val interestWrittenOff: Double,
        val interestOutstanding: Double,
        val interestOverdue: Int,
        val feeChargesCharged: Double,
        val feeChargesDueAtDisbursementCharged: Double,
        val feeChargesPaid: Double,
        val feeChargesWaived: Double,
        val feeChargesWrittenOff: Double,
        val feeChargesOutstanding: Double,
        val feeChargesOverdue: Int,
        val penaltyChargesCharged: Double,
        val penaltyChargesPaid: Double,
        val penaltyChargesWaived: Double,
        val penaltyChargesWrittenOff: Double,
        val penaltyChargesOutstanding: Double,
        val penaltyChargesOverdue: Int,
        val totalExpectedRepayment: Double,
        val totalRepayment: Double,
        val totalExpectedCostOfLoan: Double,
        val totalCostOfLoan: Double,
        val totalWaived: Double,
        val totalWrittenOff: Double,
        val totalOutstanding: Double,
        val totalOverdue: Int,
        val totalRecovered: Int
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Currency(
            val code: String,
            val name: String,
            val decimalPlaces: Int,
            val inMultiplesOf: Int,
            val displaySymbol: String,
            val nameCode: String,
            val displayLabel: String
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class DaysInMonthType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class DaysInYearType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestRecalculationData(
        val id: Int,
        val loanId: Int,
        val interestRecalculationCompoundingType: InterestRecalculationCompoundingType,
        val rescheduleStrategyType: RescheduleStrategyType,
        val calendarData: CalendarData,
        val recalculationRestFrequencyType: RecalculationRestFrequencyType,
        val recalculationRestFrequencyInterval: Int,
        val recalculationCompoundingFrequencyType: RecalculationCompoundingFrequencyType,
        val isCompoundingToBePostedAsTransaction: Boolean,
        val allowCompoundingOnEod: Boolean
    ) : Parcelable {
        @Keep
        @Parcelize
        data class InterestRecalculationCompoundingType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class RescheduleStrategyType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class CalendarData(
            val id: Int,
            val calendarInstanceId: Int,
            val entityId: Int,
            val entityType: EntityType,
            val title: String,
            val startDate: List<Int>,
            val endDate: List<Int>,
            val duration: Int,
            val type: Type,
            val repeating: Boolean,
            val recurrence: String,
            val frequency: Frequency,
            val interval: Int,
            val repeatsOnNthDayOfMonth: RepeatsOnNthDayOfMonth,
            val firstReminder: Int,
            val secondReminder: Int,
            val humanReadable: String,
            val createdDate: List<Int>,
            val lastUpdatedDate: List<Int>,
            val createdByUserId: Int,
            val createdByUsername: String,
            val lastUpdatedByUserId: Int,
            val lastUpdatedByUsername: String
        ) : Parcelable {
            @Keep
            @Parcelize
            data class EntityType(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable

            @Keep
            @Parcelize
            data class Type(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable

            @Keep
            @Parcelize
            data class Frequency(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable

            @Keep
            @Parcelize
            data class RepeatsOnNthDayOfMonth(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable
        }

        @Keep
        @Parcelize
        data class RecalculationRestFrequencyType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class RecalculationCompoundingFrequencyType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class PaidInAdvance(
        val paidInAdvance: Double
    ) : Parcelable

    companion object : DiffUtil.ItemCallback<ConservativeLoanAccount>() {
        override fun areItemsTheSame(
            oldItem: ConservativeLoanAccount,
            newItem: ConservativeLoanAccount
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ConservativeLoanAccount,
            newItem: ConservativeLoanAccount
        ): Boolean {
            return oldItem == newItem
        }
    }
}