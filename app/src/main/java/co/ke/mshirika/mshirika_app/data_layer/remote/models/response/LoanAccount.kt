package co.ke.mshirika.mshirika_app.data_layer.remote.models.response


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.common.Currency
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class LoanAccount(
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
    val expectedFirstRepaymentOnDate: List<Int>,
    val feeChargesAtDisbursementCharged: Double,
    val graceOnArrearsAgeing: Int,
    val id: Int,
    val inArrears: Boolean,
    val interestCalculationPeriodType: InterestCalculationPeriodType,
    val interestChargedFromDate: List<Int>,
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
    val loanType: LoanType,
    val maximumGap: Int,
    val minimumGap: Int,
    val multiDisburseLoan: Boolean,
    val numberOfRepayments: Int,
    val originalSchedule: OriginalSchedule,
    val paidInAdvance: PaidInAdvance,
    val principal: Double,
    val proposedPrincipal: Double,
    val repaymentEvery: Int,
    val repaymentFrequencyType: RepaymentFrequencyType,
    val repaymentSchedule: RepaymentSchedule,
    val status: Status,
    val summary: Summary,
    val syncDisbursementWithMeeting: Boolean,
    val termFrequency: Int,
    val termPeriodFrequencyType: TermPeriodFrequencyType,
    val timeline: Timeline,
    val transactionProcessingStrategyId: Int,
    val transactionProcessingStrategyName: String,
    val transactions: List<LoanTransaction>
) : Respondent {

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
    data class LoanTransaction(
        val amount: Double,
        val currency: Currency,
        val date: List<Int>,
        val feeChargesPortion: Int,
        val id: Int,
        val interestPortion: Int,
        val manuallyReversed: Boolean,
        val officeId: Int,
        val officeName: String,
        val outstandingLoanBalance: Double,
        val overpaymentPortion: Int,
        val paymentDetailData: PaymentDetailData,
        val penaltyChargesPortion: Int,
        val principalPortion: Double,
        val submittedByUsername: String,
        val submittedOnDate: List<Int>,
        val transfer: Transfer,
        val type: LoanTransactionType,
        val unrecognizedIncomePortion: Int
    ) : Parcelable {
        companion object : DiffUtil.ItemCallback<LoanTransaction>() {
            override fun areItemsTheSame(
                oldItem: LoanTransaction,
                newItem: LoanTransaction
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: LoanTransaction,
                newItem: LoanTransaction
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    @Parcelize
    @Keep
    data class LoanTransactionType(
        val accrual: Boolean,
        val approveTransfer: Boolean,
        val chargePayment: Boolean,
        val code: String,
        val contra: Boolean,
        val disbursement: Boolean,
        val id: Int,
        val initiateTransfer: Boolean,
        val recoveryRepayment: Boolean,
        val refund: Boolean,
        val refundForActiveLoans: Boolean,
        val rejectTransfer: Boolean,
        val repayment: Boolean,
        val repaymentAtDisbursement: Boolean,
        val value: String,
        val waiveCharges: Boolean,
        val waiveInterest: Boolean,
        val withdrawTransfer: Boolean,
        val writeOff: Boolean
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
    data class OriginalSchedule(
        val currency: Currency,
        val loanTermInDays: Int,
        val periods: List<Period>,
        val totalFeeChargesCharged: Int,
        val totalInterestCharged: Int,
        val totalPenaltyChargesCharged: Int,
        val totalPrincipalDisbursed: Int,
        val totalPrincipalExpected: Int,
        val totalRepaymentExpected: Int
    ) : Parcelable

    @Parcelize
    @Keep
    data class PaidInAdvance(
        val paidInAdvance: Int
    ) : Parcelable

    @Parcelize
    @Keep
    data class PaymentDetailData(
        val accountNumber: String,
        val bankNumber: String,
        val checkNumber: String,
        val id: Int,
        val paymentType: PaymentType,
        val receiptNumber: String,
        val routingCode: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class PaymentType(
        val id: Int,
        val name: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class Period(
        val daysInPeriod: Int,
        val dueDate: List<Int>,
        val feeChargesDue: Double,
        val feeChargesPaid: Double,
        val fromDate: List<Int>,
        val interestDue: Double,
        val interestOriginalDue: Double,
        val interestOutstanding: Double,
        val penaltyChargesDue: Double,
        val period: Int,
        val principalDisbursed: Double,
        val principalDue: Double,
        val principalLoanBalanceOutstanding: Double,
        val principalOriginalDue: Double,
        val principalOutstanding: Double,
        val totalActualCostOfLoanForPeriod: Double,
        val totalDueForPeriod: Double,
        val totalInstallmentAmountForPeriod: Int,
        val totalOriginalDueForPeriod: Double,
        val totalOutstandingForPeriod: Double,
        val totalOverdue: Double,
        val totalPaidForPeriod: Double
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
    data class RepaymentPeriod(
        val complete: Boolean,
        val daysInPeriod: Int,
        val dueDate: List<Int>,
        val feeChargesDue: Double,
        val feeChargesOutstanding: Int,
        val feeChargesPaid: Double,
        val feeChargesWaived: Int,
        val feeChargesWrittenOff: Int,
        val fromDate: List<Int>,
        val interestDue: Double,
        val interestOriginalDue: Double,
        val interestOutstanding: Double,
        val interestPaid: Double,
        val interestWaived: Int,
        val interestWrittenOff: Int,
        val obligationsMetOnDate: List<Int>,
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
        val totalDueForPeriod: Double,
        val totalInstallmentAmountForPeriod: Int,
        val totalOriginalDueForPeriod: Double,
        val totalOutstandingForPeriod: Double,
        val totalOverdue: Double,
        val totalPaidForPeriod: Double,
        val totalPaidInAdvanceForPeriod: Double,
        val totalPaidLateForPeriod: Double,
        val totalWaivedForPeriod: Int,
        val totalWrittenOffForPeriod: Int
    ) : Parcelable

    @Parcelize
    @Keep
    data class RepaymentSchedule(
        val currency: Currency,
        val loanTermInDays: Int,
        val periods: List<RepaymentPeriod>,
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
    data class Transfer(
        val currency: Currency,
        val id: Int,
        val reversed: Boolean,
        val transferAmount: Double,
        val transferDate: List<Int>,
        val transferDescription: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class Type(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    companion object : DiffUtil.ItemCallback<LoanAccount>() {
        override fun areItemsTheSame(oldItem: LoanAccount, newItem: LoanAccount): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LoanAccount, newItem: LoanAccount): Boolean {
            return oldItem == newItem
        }
    }
}