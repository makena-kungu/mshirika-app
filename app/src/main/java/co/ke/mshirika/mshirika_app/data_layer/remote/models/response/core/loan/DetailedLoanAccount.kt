package co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DetailedLoanAccount(
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
    val repaymentSchedule: RepaymentSchedule,
    val transactions: List<Transaction>,
    //val disbursementDetails: List<Any>,
    val originalSchedule: OriginalSchedule,
    val feeChargesAtDisbursementCharged: Double,
    val loanProductCounter: Int,
    val multiDisburseLoan: Boolean,
    val canDefineInstallmentAmount: Boolean,
    val canDisburse: Boolean,
    //val emiAmountVariations: List<Any>,
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
    data class RepaymentSchedule(
        val currency: Currency,
        val loanTermInDays: Int,
        val totalPrincipalDisbursed: Int,
        val totalPrincipalExpected: Int,
        val totalPrincipalPaid: Int,
        val totalInterestCharged: Int,
        val totalFeeChargesCharged: Int,
        val totalPenaltyChargesCharged: Int,
        val totalWaived: Int,
        val totalWrittenOff: Int,
        val totalRepaymentExpected: Int,
        val totalRepayment: Int,
        val totalPaidInAdvance: Int,
        val totalPaidLate: Int,
        val totalOutstanding: Int,
        val periods: List<Period>
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

        @Keep
        @Parcelize
        data class Period(
            val dueDate: List<Int>,
            val principalDisbursed: Double,
            val principalLoanBalanceOutstanding: Double,
            val feeChargesDue: Double,
            val feeChargesPaid: Double,
            val totalOriginalDueForPeriod: Double,
            val totalDueForPeriod: Double,
            val totalPaidForPeriod: Double,
            val totalActualCostOfLoanForPeriod: Double,
            val period: Int,
            val fromDate: List<Int>,
            val obligationsMetOnDate: List<Int>,
            val complete: Boolean,
            val daysInPeriod: Int,
            val principalOriginalDue: Double,
            val principalDue: Double,
            val principalPaid: Double,
            val principalWrittenOff: Int,
            val principalOutstanding: Double,
            val interestOriginalDue: Double,
            val interestDue: Double,
            val interestPaid: Double,
            val interestWaived: Int,
            val interestWrittenOff: Int,
            val interestOutstanding: Double,
            val feeChargesWaived: Int,
            val feeChargesWrittenOff: Int,
            val feeChargesOutstanding: Int,
            val penaltyChargesDue: Int,
            val penaltyChargesPaid: Int,
            val penaltyChargesWaived: Int,
            val penaltyChargesWrittenOff: Int,
            val penaltyChargesOutstanding: Int,
            val totalPaidInAdvanceForPeriod: Double,
            val totalPaidLateForPeriod: Double,
            val totalWaivedForPeriod: Int,
            val totalWrittenOffForPeriod: Int,
            val totalOutstandingForPeriod: Double,
            val totalOverdue: Double,
            val totalInstallmentAmountForPeriod: Int
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class Transaction(
        val id: Int,
        val officeId: Int,
        val officeName: String,
        val type: Type,
        val date: List<Int>,
        val currency: Currency,
        val paymentDetailData: PaymentDetailData,
        val amount: Double,
        val principalPortion: Double,
        val interestPortion: Double,
        val feeChargesPortion: Int,
        val penaltyChargesPortion: Int,
        val overpaymentPortion: Int,
        val unrecognizedIncomePortion: Int,
        val outstandingLoanBalance: Double,
        val submittedOnDate: List<Int>,
        val manuallyReversed: Boolean,
        val submittedByUsername: String,
        val transfer: Transfer
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Type(
            val id: Int,
            val code: String,
            val value: String,
            val disbursement: Boolean,
            val repaymentAtDisbursement: Boolean,
            val repayment: Boolean,
            val contra: Boolean,
            val waiveInterest: Boolean,
            val waiveCharges: Boolean,
            val accrual: Boolean,
            val writeOff: Boolean,
            val recoveryRepayment: Boolean,
            val initiateTransfer: Boolean,
            val approveTransfer: Boolean,
            val withdrawTransfer: Boolean,
            val rejectTransfer: Boolean,
            val chargePayment: Boolean,
            val refund: Boolean,
            val refundForActiveLoans: Boolean
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
        data class PaymentDetailData(
            val id: Int,
            val paymentType: PaymentType,
            val accountNumber: String,
            val checkNumber: String,
            val routingCode: String,
            val receiptNumber: String,
            val bankNumber: String
        ) : Parcelable {
            @Keep
            @Parcelize
            data class PaymentType(
                val id: Int,
                val name: String
            ) : Parcelable
        }

        @Keep
        @Parcelize
        data class Transfer(
            val id: Int,
            val reversed: Boolean,
            val currency: Currency,
            val transferAmount: Double,
            val transferDate: List<Int>,
            val transferDescription: String
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

        companion object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem == newItem
            }
        }
    }

    @Keep
    @Parcelize
    data class OriginalSchedule(
        val currency: Currency,
        val loanTermInDays: Int,
        val totalPrincipalDisbursed: Int,
        val totalPrincipalExpected: Int,
        val totalInterestCharged: Int,
        val totalFeeChargesCharged: Int,
        val totalPenaltyChargesCharged: Int,
        val totalRepaymentExpected: Int,
        val periods: List<Period>
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

        @Keep
        @Parcelize
        data class Period(
            val dueDate: List<Int>,
            val principalDisbursed: Double,
            val principalLoanBalanceOutstanding: Double,
            val feeChargesDue: Double,
            val feeChargesPaid: Double,
            val totalOriginalDueForPeriod: Double,
            val totalDueForPeriod: Double,
            val totalPaidForPeriod: Double,
            val totalActualCostOfLoanForPeriod: Double,
            val period: Int,
            val fromDate: List<Int>,
            val daysInPeriod: Int,
            val principalOriginalDue: Double,
            val principalDue: Double,
            val principalOutstanding: Double,
            val interestOriginalDue: Double,
            val interestDue: Double,
            val interestOutstanding: Double,
            val penaltyChargesDue: Double,
            val totalOutstandingForPeriod: Double,
            val totalOverdue: Double,
            val totalInstallmentAmountForPeriod: Int
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
}