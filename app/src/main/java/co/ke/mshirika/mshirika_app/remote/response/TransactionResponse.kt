package co.ke.mshirika.mshirika_app.remote.response


import android.os.Parcelable
import co.ke.mshirika.mshirika_app.data.response.Transaction
import co.ke.mshirika.mshirika_app.data.response.common.Currency
import co.ke.mshirika.mshirika_app.remote.response.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionResponse(
    val accountNo: String,
    val allowOverdraft: Boolean,
    val blockNarration: BlockNarration,
    val blockNarrationOptions: List<BlockNarrationOption>,
    val clientId: Int,
    val clientName: String,
    val currency: Currency,
    val depositType: DepositType,
    val enforceMinRequiredBalance: Boolean,
    val fieldOfficerId: Int,
    val fieldOfficerName: String,
    val id: Int,
    val interestCalculationDaysInYearType: InterestCalculationDaysInYearType,
    val interestCalculationType: InterestCalculationType,
    val interestCompoundingPeriodType: InterestCompoundingPeriodType,
    val interestPostingPeriodType: InterestPostingPeriodType,
    val isDormancyTrackingActive: Boolean,
    val lastActiveTransactionDate: List<Int>,
    val nominalAnnualInterestRate: Double,
    val onHoldFunds: Double,
    val releaseguarantor: Boolean,
    val savingsProductId: Int,
    val savingsProductName: String,
    val status: Status,
    val subStatus: SubStatus,
    val summary: Summary,
    val timeline: Timeline,
    val transactions: List<Transaction>,
    val withHoldTax: Boolean,
    val withdrawalFeeForTransfers: Boolean
) : Parcelable, Respondent {

    @Parcelize
    data class BlockNarration(
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Parcelize
    data class InterestCalculationType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    data class BlockNarrationOption(
        val active: Boolean,
        val description: String,
        val id: Int,
        val mandatory: Boolean,
        val name: String,
        val position: Int
    ) : Parcelable

    @Parcelize
    data class Status(
        val active: Boolean,
        val approved: Boolean,
        val closed: Boolean,
        val code: String,
        val id: Int,
        val matured: Boolean,
        val prematureClosed: Boolean,
        val rejected: Boolean,
        val submittedAndPendingApproval: Boolean,
        val transferInProgress: Boolean,
        val transferOnHold: Boolean,
        val value: String,
        val withdrawnByApplicant: Boolean
    ) : Parcelable

    @Parcelize
    data class Summary(
        val accountBalance: Double,
        val availableBalance: Double,
        val currency: Currency,
        val interestNotPosted: Int,
        val totalDeposits: Double,
        val totalInterestPosted: Int,
        val totalOverdraftInterestDerived: Int
    ) : Parcelable

    @Parcelize
    data class InterestPostingPeriodType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    data class SubStatus(
        val block: Boolean,
        val blockCredit: Boolean,
        val blockDebit: Boolean,
        val code: String,
        val dormant: Boolean,
        val escheat: Boolean,
        val id: Int,
        val inactive: Boolean,
        val none: Boolean,
        val value: String
    ) : Parcelable

    @Parcelize
    data class DepositType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    data class InterestCompoundingPeriodType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    data class Timeline(
        val activatedByFirstname: String,
        val activatedByLastname: String,
        val activatedByUsername: String,
        val activatedOnDate: List<Int>,
        val approvedByFirstname: String,
        val approvedByLastname: String,
        val approvedByUsername: String,
        val approvedOnDate: List<Int>,
        val submittedByFirstname: String,
        val submittedByLastname: String,
        val submittedByUsername: String,
        val submittedOnDate: List<Int>
    ) : Parcelable

    @Parcelize
    data class InterestCalculationDaysInYearType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable
}