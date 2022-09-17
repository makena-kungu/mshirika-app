package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.common.Currency
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class LoanRepaymentSchedule(
    val accountNo: String,
    val allowPartialPeriodInterestCalcualtion: Boolean,
    val amortizationType: AmortizationType,
    val annualInterestRate: Double,
    val approvedPrincipal: Double,
    val canDefineInstallmentAmount: Boolean,
    val canDisburse: Boolean,
    val canUseForTopup: Boolean,
    val clientAccountNo: String,
    val clientId: Int,
    val clientName: String,
    val clientOfficeId: Int,
    val createStandingInstructionAtDisbursement: Boolean,
    val currency: Currency,
    val daysInMonthType: DaysInMonthType,
    val daysInYearType: DaysInYearType,
    val feeChargesAtDisbursementCharged: Double,
    val fundId: Int,
    val fundName: String,
    val graceOnArrearsAgeing: Int,
    val id: Int,
    val inArrears: Boolean,
    val interestCalculationPeriodType: InterestCalculationPeriodType,
    val interestRateFrequencyType: InterestRateFrequencyType,
    val interestRatePerPeriod: Double,
    val interestRecalculationData: InterestRecalculationData,
    val interestType: InterestType,
    val isEqualAmortization: Boolean,
    val isFloatingInterestRate: Boolean,
    val isInterestRecalculationEnabled: Boolean,
    val isLoanProductLinkedToFloatingRate: Boolean,
    val isNPA: Boolean,
    val isRatesEnabled: Boolean,
    val isTopup: Boolean,
    val isVariableInstallmentsAllowed: Boolean,
    val loanIdToClose: Int,
    val loanOfficerId: Int,
    val loanOfficerName: String,
    val loanProductCounter: Int,
    val loanProductDescription: String,
    val loanProductId: Int,
    val loanProductName: String,
    val loanPurposeId: Int,
    val loanPurposeName: String,
    val loanType: LoanType,
    val maximumGap: Int,
    val minimumGap: Int,
    val multiDisburseLoan: Boolean,
    val numberOfRepayments: Int,
    val paidInAdvance: PaidInAdvance,
    val principal: Double,
    val proposedPrincipal: Double,
    val repaymentEvery: Int,
    val repaymentFrequencyType: RepaymentFrequencyType,
    val repaymentSchedule: RepaymentSchedule?,
    val status: Status,
    val summary: Summary,
    val syncDisbursementWithMeeting: Boolean,
    val termFrequency: Int,
    val termPeriodFrequencyType: TermPeriodFrequencyType,
    val timeline: Timeline,
    val transactionProcessingStrategyId: Int,
    val transactionProcessingStrategyName: String
) : Parcelable, Respondent {

    @Parcelize
    @Keep
    data class AmortizationType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class CalendarData(
        val calendarInstanceId: Int,
        val createdByUserId: Int,
        val createdByUsername: String,
        val createdDate: List<Int>,
        val duration: Int,
        val endDate: List<Int>,
        val entityId: Int,
        val entityType: EntityType,
        val firstReminder: Int,
        val frequency: Frequency,
        val humanReadable: String,
        val id: Int,
        val interval: Int,
        val lastUpdatedByUserId: Int,
        val lastUpdatedByUsername: String,
        val lastUpdatedDate: List<Int>,
        val recurrence: String,
        val repeating: Boolean,
        val repeatsOnNthDayOfMonth: RepeatsOnNthDayOfMonth,
        val secondReminder: Int,
        val startDate: List<Int>,
        val title: String,
        val type: Type
    ) : Parcelable

    @Parcelize
    @Keep
    data class DaysInMonthType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class DaysInYearType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class EntityType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class Frequency(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class InterestCalculationPeriodType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class InterestRateFrequencyType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class InterestRecalculationCompoundingType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class InterestRecalculationData(
        val allowCompoundingOnEod: Boolean,
        val calendarData: CalendarData,
        val id: Int,
        val interestRecalculationCompoundingType: InterestRecalculationCompoundingType,
        val isCompoundingToBePostedAsTransaction: Boolean,
        val loanId: Int,
        val recalculationCompoundingFrequencyType: RecalculationCompoundingFrequencyType,
        val recalculationRestFrequencyInterval: Int,
        val recalculationRestFrequencyType: RecalculationRestFrequencyType,
        val rescheduleStrategyType: RescheduleStrategyType
    ) : Parcelable

    @Parcelize
    @Keep
    data class InterestType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class LoanType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class PaidInAdvance(
        val paidInAdvance: Int
    ) : Parcelable

    @Parcelize
    @Keep
    data class Period(
        val complete: Boolean,
        val daysInPeriod: Int,
        val dueDate: List<Int>?,
        val feeChargesDue: Double,
        val feeChargesOutstanding: Int,
        val feeChargesPaid: Double,
        val feeChargesWaived: Int,
        val feeChargesWrittenOff: Int,
        val fromDate: List<Int>?,
        val interestDue: Double,
        val interestOriginalDue: Double,
        val interestOutstanding: Double,
        val interestPaid: Double,
        val interestWaived: Int,
        val interestWrittenOff: Int,
        val penaltyChargesDue: Int,
        val penaltyChargesOutstanding: Int,
        val penaltyChargesPaid: Int,
        val penaltyChargesWaived: Int,
        val penaltyChargesWrittenOff: Int,
        val period: Int,
        val principalDisbursed: Double,
        val principalDue: Double,
        val principalLoanBalanceOutstanding: Double,
        val principalOriginalDue: Double,
        val principalOutstanding: Double,
        val principalPaid: Double,
        val principalWrittenOff: Int,
        val totalActualCostOfLoanForPeriod: Double,
        val totalDueForPeriod: Double,//this is the figure we should display
        val totalInstallmentAmountForPeriod: Int,
        val totalOriginalDueForPeriod: Double,
        val totalOutstandingForPeriod: Double,
        val totalOverdue: Double,
        val totalPaidForPeriod: Double,
        val totalPaidInAdvanceForPeriod: Double,
        val totalPaidLateForPeriod: Int,
        val totalWaivedForPeriod: Int,
        val totalWrittenOffForPeriod: Int
    ) : Parcelable

    @Parcelize
    @Keep
    data class RecalculationCompoundingFrequencyType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class RecalculationRestFrequencyType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class RepaymentFrequencyType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class RepaymentSchedule(
        val currency: Currency,
        val loanTermInDays: Int,
        val periods: List<Period>?,
        val totalFeeChargesCharged: Int,
        val totalInterestCharged: Int,
        val totalOutstanding: Int,
        val totalPaidInAdvance: Int,
        val totalPaidLate: Int,
        val totalPenaltyChargesCharged: Int,
        val totalPrincipalDisbursed: Int,
        val totalPrincipalExpected: Int,
        val totalPrincipalPaid: Int,
        val totalRepayment: Int,
        val totalRepaymentExpected: Int,
        val totalWaived: Int,
        val totalWrittenOff: Int
    ) : Parcelable

    @Parcelize
    @Keep
    data class RepeatsOnNthDayOfMonth(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class RescheduleStrategyType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class Status(
        val active: Boolean,
        val closed: Boolean,
        val closedObligationsMet: Boolean,
        val closedRescheduled: Boolean,
        val closedWrittenOff: Boolean,
        val code: String,
        val id: Int,
        val overpaid: Boolean,
        val pendingApproval: Boolean,
        val value: String,
        val waitingForDisbursal: Boolean
    ) : Parcelable

    @Parcelize
    @Keep
    data class Summary(
        val currency: Currency,
        val feeChargesCharged: Double,
        val feeChargesDueAtDisbursementCharged: Double,
        val feeChargesOutstanding: Double,
        val feeChargesOverdue: Int,
        val feeChargesPaid: Double,
        val feeChargesWaived: Double,
        val feeChargesWrittenOff: Double,
        val interestCharged: Double,
        val interestOutstanding: Double,
        val interestOverdue: Int,
        val interestPaid: Double,
        val interestWaived: Double,
        val interestWrittenOff: Double,
        val penaltyChargesCharged: Double,
        val penaltyChargesOutstanding: Double,
        val penaltyChargesOverdue: Int,
        val penaltyChargesPaid: Double,
        val penaltyChargesWaived: Double,
        val penaltyChargesWrittenOff: Double,
        val principalDisbursed: Double,
        val principalOutstanding: Double,
        val principalOverdue: Int,
        val principalPaid: Double,
        val principalWrittenOff: Double,
        val totalCostOfLoan: Double,
        val totalExpectedCostOfLoan: Double,
        val totalExpectedRepayment: Double,
        val totalOutstanding: Double,
        val totalOverdue: Int,
        val totalRecovered: Int,
        val totalRepayment: Double,
        val totalWaived: Double,
        val totalWrittenOff: Double
    ) : Parcelable

    @Parcelize
    @Keep
    data class TermPeriodFrequencyType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class Timeline(
        val actualDisbursementDate: List<Int>,
        val approvedByFirstname: String,
        val approvedByLastname: String,
        val approvedByUsername: String,
        val approvedOnDate: List<Int>,
        val disbursedByFirstname: String,
        val disbursedByLastname: String,
        val disbursedByUsername: String,
        val expectedDisbursementDate: List<Int>,
        val expectedMaturityDate: List<Int>,
        val submittedByFirstname: String,
        val submittedByLastname: String,
        val submittedByUsername: String,
        val submittedOnDate: List<Int>
    ) : Parcelable

    @Parcelize
    @Keep
    data class Type(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    companion object : DiffUtil.ItemCallback<LoanRepaymentSchedule>() {
        override fun areItemsTheSame(
            oldItem: LoanRepaymentSchedule,
            newItem: LoanRepaymentSchedule
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: LoanRepaymentSchedule,
            newItem: LoanRepaymentSchedule
        ): Boolean = oldItem == newItem
    }
}