package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavingsAccount(
    val accountBalance: Double,
    val accountNo: String,
    val accountType: AccountType,
    val currency: Currency,
    val depositType: DepositType,
    val id: Int,
    val lastActiveTransactionDate: List<Int>,
    val productId: Int,
    val productName: String,
    val shortProductName: String,
    val status: Status,
    val subStatus: SubStatus,
    val timeline: Timeline
) : Parcelable {

    @Parcelize
    data class AccountType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable {

    }

    @Parcelize
    data class Currency(
        val code: String,
        val decimalPlaces: Int,
        val displayLabel: String,
        val displaySymbol: String,
        val inMultiplesOf: Int,
        val name: String,
        val nameCode: String
    ) : Parcelable

    @Parcelize
    data class DepositType(
        val code: String,
        val id: Int,
        val value: String
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
    data class Timeline(
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

    companion object : DiffUtil.ItemCallback<SavingsAccount>() {
        override fun areItemsTheSame(
            oldItem: SavingsAccount,
            newItem: SavingsAccount
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SavingsAccount,
            newItem: SavingsAccount
        ) = oldItem == newItem
    }
}