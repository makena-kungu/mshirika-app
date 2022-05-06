package co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent

@Keep
@Parcelize
data class NewLoanTemplate2(
    val clientId: Int,
    val clientAccountNo: String,
    val clientName: String,
    val clientOfficeId: Int,
    val loanProductId: Int,
    val loanProductName: String,
    val loanProductDescription: String,
    val isLoanProductLinkedToFloatingRate: Boolean,
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
    val graceOnArrearsAgeing: Int,
    val timeline: Timeline,
    val productOptions: List<ProductOption>,
    val loanOfficerOptions: List<LoanOfficerOption>,
    val loanPurposeOptions: List<LoanPurposeOption>,
    val fundOptions: List<FundOption>,
    val termFrequencyTypeOptions: List<TermFrequencyTypeOption>,
    val repaymentFrequencyTypeOptions: List<RepaymentFrequencyTypeOption>,
    val repaymentFrequencyNthDayTypeOptions: List<RepaymentFrequencyNthDayTypeOption>,
    val repaymentFrequencyDaysOfWeekTypeOptions: List<RepaymentFrequencyDaysOfWeekTypeOption>,
    val interestRateFrequencyTypeOptions: List<InterestRateFrequencyTypeOption>,
    val amortizationTypeOptions: List<AmortizationTypeOption>,
    val interestTypeOptions: List<InterestTypeOption>,
    val interestCalculationPeriodTypeOptions: List<InterestCalculationPeriodTypeOption>,
    val transactionProcessingStrategyOptions: List<TransactionProcessingStrategyOption>,
    val chargeOptions: List<ChargeOption>,
    val multiDisburseLoan: Boolean,
    val canDefineInstallmentAmount: Boolean,
    val canDisburse: Boolean,
    val clientActiveLoanOptions: List<ClientActiveLoanOption>,
    val canUseForTopup: Boolean,
    val isTopup: Boolean,
    val product: Product,
    val overdueCharges: List<String> = emptyList(),
    val daysInMonthType: DaysInMonthType,
    val daysInYearType: DaysInYearType,
    val isInterestRecalculationEnabled: Boolean,
    val interestRecalculationData: InterestRecalculationData,
    val isVariableInstallmentsAllowed: Boolean,
    val minimumGap: Int,
    val maximumGap: Int,
    val isEqualAmortization: Boolean,
    val isRatesEnabled: Boolean
) : Respondent {
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
        val expectedDisbursementDate: List<Int>
    ) : Parcelable

    @Keep
    @Parcelize
    data class ProductOption(
        val id: Int,
        val name: String,
        val includeInBorrowerCycle: Boolean,
        val useBorrowerCycle: Boolean,
        val isLinkedToFloatingInterestRates: Boolean,
        val isFloatingInterestRateCalculationAllowed: Boolean,
        val allowVariableInstallments: Boolean,
        val isInterestRecalculationEnabled: Boolean,
        val canDefineInstallmentAmount: Boolean,
        val principalVariationsForBorrowerCycle: List<String> = emptyList(),
        val interestRateVariationsForBorrowerCycle: List<String> = emptyList(),
        val numberOfRepaymentVariationsForBorrowerCycle: List<String> = emptyList(),
        val canUseForTopup: Boolean,
        val isRatesEnabled: Boolean,
        val multiDisburseLoan: Boolean,
        val holdGuaranteeFunds: Boolean,
        val accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean,
        val syncExpectedWithDisbursementDate: Boolean,
        val isEqualAmortization: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class LoanOfficerOption(
        val id: Int,
        val firstname: String,
        val lastname: String,
        val displayName: String,
        val mobileNo: String,
        val officeId: Int,
        val officeName: String,
        val isLoanOfficer: Boolean,
        val isActive: Boolean,
        val joiningDate: List<Int>
    ) : Parcelable

    @Keep
    @Parcelize
    data class LoanPurposeOption(
        val id: Int,
        val name: String,
        val position: Int,
        val description: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class FundOption(
        val id: Int,
        val name: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class TermFrequencyTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class RepaymentFrequencyTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class RepaymentFrequencyNthDayTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class RepaymentFrequencyDaysOfWeekTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestRateFrequencyTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class AmortizationTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestCalculationPeriodTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class TransactionProcessingStrategyOption(
        val id: Int,
        val code: String,
        val name: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ChargeOption(
        val id: Int,
        val name: String,
        val active: Boolean,
        val penalty: Boolean,
        val currency: Currency,
        val amount: Double,
        val chargeTimeType: ChargeTimeType,
        val chargeAppliesTo: ChargeAppliesTo,
        val chargeCalculationType: ChargeCalculationType,
        val chargePaymentMode: ChargePaymentMode
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Currency(
            val code: String,
            val name: String,
            val decimalPlaces: Int,
            val displaySymbol: String,
            val nameCode: String,
            val displayLabel: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ChargeTimeType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ChargeAppliesTo(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ChargeCalculationType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ChargePaymentMode(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class ClientActiveLoanOption(
        val id: Int,
        val accountNo: String,
        val productId: Int,
        val productName: String,
        val shortProductName: String,
        val status: Status,
        val loanType: LoanType,
        val loanCycle: Int,
        val timeline: Timeline,
        val inArrears: Boolean,
        val originalLoan: Double,
        val loanBalance: Double,
        val amountPaid: Double
    ) : Parcelable {
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
    }

    @Keep
    @Parcelize
    data class Product(
        val id: Int,
        val name: String,
        val shortName: String,
        val description: String,
        val includeInBorrowerCycle: Boolean,
        val useBorrowerCycle: Boolean,
        val status: String,
        val currency: Currency,
        val principal: Double,
        val minPrincipal: Double,
        val maxPrincipal: Double,
        val numberOfRepayments: Int,
        val minNumberOfRepayments: Int,
        val maxNumberOfRepayments: Int,
        val repaymentEvery: Int,
        val repaymentFrequencyType: RepaymentFrequencyType,
        val interestRatePerPeriod: Double,
        val minInterestRatePerPeriod: Double,
        val maxInterestRatePerPeriod: Double,
        val interestRateFrequencyType: InterestRateFrequencyType,
        val annualInterestRate: Double,
        val isLinkedToFloatingInterestRates: Boolean,
        val isFloatingInterestRateCalculationAllowed: Boolean,
        val allowVariableInstallments: Boolean,
        val minimumGap: Int,
        val maximumGap: Int,
        val amortizationType: AmortizationType,
        val interestType: InterestType,
        val interestCalculationPeriodType: InterestCalculationPeriodType,
        val allowPartialPeriodInterestCalcualtion: Boolean,
        val transactionProcessingStrategyId: Int,
        val transactionProcessingStrategyName: String,
        val graceOnArrearsAgeing: Int,
        val daysInMonthType: DaysInMonthType,
        val daysInYearType: DaysInYearType,
        val isInterestRecalculationEnabled: Boolean,
        val interestRecalculationData: InterestRecalculationData,
        val canDefineInstallmentAmount: Boolean,
        val installmentAmountInMultiplesOf: Int,
        val charges: List<String> = emptyList(),
        val principalVariationsForBorrowerCycle: List<String> = emptyList(),
        val interestRateVariationsForBorrowerCycle: List<String> = emptyList(),
        val numberOfRepaymentVariationsForBorrowerCycle: List<String> = emptyList(),
        val accountingRule: AccountingRule,
        val canUseForTopup: Boolean,
        val isRatesEnabled: Boolean,
        val rates: List<String> = emptyList(),
        val multiDisburseLoan: Boolean,
        val maxTrancheCount: Int,
        val principalThresholdForLastInstallment: Double,
        val holdGuaranteeFunds: Boolean,
        val productGuaranteeData: ProductGuaranteeData,
        val accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean,
        val allowAttributeOverrides: AllowAttributeOverrides,
        val syncExpectedWithDisbursementDate: Boolean,
        val isEqualAmortization: Boolean
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
            val productId: Int,
            val interestRecalculationCompoundingType: InterestRecalculationCompoundingType,
            val rescheduleStrategyType: RescheduleStrategyType,
            val recalculationRestFrequencyType: RecalculationRestFrequencyType,
            val recalculationRestFrequencyInterval: Int,
            val isArrearsBasedOnOriginalSchedule: Boolean,
            val isCompoundingToBePostedAsTransaction: Boolean,
            val preClosureInterestCalculationStrategy: PreClosureInterestCalculationStrategy,
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
            data class RecalculationRestFrequencyType(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable

            @Keep
            @Parcelize
            data class PreClosureInterestCalculationStrategy(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable
        }

        @Keep
        @Parcelize
        data class AccountingRule(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ProductGuaranteeData(
            val id: Int,
            val productId: Int,
            val mandatoryGuarantee: Double,
            val minimumGuaranteeFromOwnFunds: Double,
            val minimumGuaranteeFromGuarantor: Double
        ) : Parcelable

        @Keep
        @Parcelize
        data class AllowAttributeOverrides(
            val amortizationType: Boolean,
            val interestType: Boolean,
            val transactionProcessingStrategyId: Boolean,
            val interestCalculationPeriodType: Boolean,
            val inArrearsTolerance: Boolean,
            val repaymentEvery: Boolean,
            val graceOnPrincipalAndInterestPayment: Boolean,
            val graceOnArrearsAgeing: Boolean
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
        val interestRecalculationCompoundingType: InterestRecalculationCompoundingType,
        val rescheduleStrategyType: RescheduleStrategyType,
        val recalculationRestFrequencyType: RecalculationRestFrequencyType,
        val recalculationRestFrequencyInterval: Int,
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
        data class RecalculationRestFrequencyType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable
    }
}