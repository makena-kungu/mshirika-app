package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DetailedSavingsAccount(
    val id: Int,
    val accountNo: String,
    val depositType: DepositType,
    val clientId: Int,
    val clientName: String,
    val savingsProductId: Int,
    val savingsProductName: String,
    val fieldOfficerId: Int,
    val fieldOfficerName: String,
    val status: Status,
    val subStatus: SubStatus,
    val timeline: Timeline,
    val currency: Currency,
    val nominalAnnualInterestRate: Double,
    val interestCompoundingPeriodType: InterestCompoundingPeriodType,
    val interestPostingPeriodType: InterestPostingPeriodType,
    val interestCalculationType: InterestCalculationType,
    val interestCalculationDaysInYearType: InterestCalculationDaysInYearType,
    val withdrawalFeeForTransfers: Boolean,
    val allowOverdraft: Boolean,
    val enforceMinRequiredBalance: Boolean,
    val withHoldTax: Boolean,
    val lastActiveTransactionDate: List<Int>,
    val isDormancyTrackingActive: Boolean,
    val blockNarration: BlockNarration,
    val summary: Summary,
    val transactions: List<Transaction>,
    val charges: List<Charge>,
    val blockNarrationOptions: List<BlockNarrationOption>,
    val releaseguarantor: Boolean
) : Parcelable {
    @Keep
    @Parcelize
    data class DepositType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class Status(
        val id: Int,
        val code: String,
        val value: String,
        val submittedAndPendingApproval: Boolean,
        val approved: Boolean,
        val rejected: Boolean,
        val withdrawnByApplicant: Boolean,
        val active: Boolean,
        val closed: Boolean,
        val prematureClosed: Boolean,
        val transferInProgress: Boolean,
        val transferOnHold: Boolean,
        val matured: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class SubStatus(
        val id: Int,
        val code: String,
        val value: String,
        val none: Boolean,
        val inactive: Boolean,
        val dormant: Boolean,
        val escheat: Boolean,
        val block: Boolean,
        val blockCredit: Boolean,
        val blockDebit: Boolean
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
        val activatedOnDate: List<Int>,
        val activatedByUsername: String,
        val activatedByFirstname: String,
        val activatedByLastname: String
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
    data class InterestCompoundingPeriodType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestPostingPeriodType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestCalculationType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class InterestCalculationDaysInYearType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class BlockNarration(
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class Summary(
        val currency: Currency,
        val totalDeposits: Double,
        val totalWithdrawals: Double,
        val totalInterestPosted: Int,
        val accountBalance: Double,
        val totalFeeCharge: Double,
        val totalPenaltyCharge: Double,
        val totalOverdraftInterestDerived: Int,
        val interestNotPosted: Int,
        val availableBalance: Double
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
    data class Transaction(
        val id: Int,
        val transactionType: TransactionType,
        val accountId: Int,
        val accountNo: String,
        val date: List<Int>,
        val currency: Currency,
        val paymentDetailData: PaymentDetailData,
        val amount: Double,
        val runningBalance: Double,
        val reversed: Boolean,
        val submittedOnDate: List<Int>,
        val interestedPostedAsOn: Boolean,
        val submittedByUsername: String,
        val transfer: Transfer
    ) : Parcelable {
        @Keep
        @Parcelize
        data class TransactionType(
            val id: Int,
            val code: String,
            val value: String,
            val deposit: Boolean,
            val dividendPayout: Boolean,
            val withdrawal: Boolean,
            val interestPosting: Boolean,
            val feeDeduction: Boolean,
            val initiateTransfer: Boolean,
            val approveTransfer: Boolean,
            val withdrawTransfer: Boolean,
            val rejectTransfer: Boolean,
            val overdraftInterest: Boolean,
            val writtenoff: Boolean,
            val overdraftFee: Boolean,
            val withholdTax: Boolean,
            val escheat: Boolean,
            val amountHold: Boolean,
            val amountRelease: Boolean
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
            val bankNumber: String,
            val transactionDate: String
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
    }

    @Keep
    @Parcelize
    data class Charge(
        val id: Int,
        val chargeId: Int,
        val accountId: Int,
        val name: String,
        val chargeTimeType: ChargeTimeType,
        val dueDate: List<Int>,
        val chargeCalculationType: ChargeCalculationType,
        val percentage: Int,
        val amountPercentageAppliedTo: Int,
        val currency: Currency,
        val amount: Double,
        val amountPaid: Double,
        val amountWaived: Int,
        val amountWrittenOff: Int,
        val amountOutstanding: Double,
        val amountOrPercentage: Double,
        val penalty: Boolean,
        val isActive: Boolean
    ) : Parcelable {
        @Keep
        @Parcelize
        data class ChargeTimeType(
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
        data class Currency(
            val code: String,
            val name: String,
            val decimalPlaces: Int,
            val displaySymbol: String,
            val nameCode: String,
            val displayLabel: String
        ) : Parcelable

        companion object : DiffUtil.ItemCallback<Charge>() {
            override fun areItemsTheSame(oldItem: Charge, newItem: Charge): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Charge, newItem: Charge): Boolean {
                return oldItem == newItem
            }
        }
    }

    @Keep
    @Parcelize
    data class BlockNarrationOption(
        val id: Int,
        val name: String,
        val position: Int,
        val description: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable
}