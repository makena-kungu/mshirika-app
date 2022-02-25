package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoanAccount(
    val accountNo: String,
    val amountPaid: Double,
    val id: Int,
    val inArrears: Boolean,
    val loanCycle: Int,
    val loanType: LoanType,
    val originalLoan: Double,
    val productId: Int,
    val productName: String,
    val shortProductName: String,
    val status: Status,
    val timeline: Timeline
) : Parcelable {

    @Parcelize
    data class LoanType(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
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
    data class Timeline(
        val actualDisbursementDate: List<Int>,
        val approvedByFirstname: String,
        val approvedByLastname: String,
        val approvedByUsername: String,
        val approvedOnDate: List<Int>,
        val closedOnDate: List<Int>,
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

    companion object : DiffUtil.ItemCallback<LoanAccount>() {
        override fun areItemsTheSame(oldItem: LoanAccount, newItem: LoanAccount) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: LoanAccount, newItem: LoanAccount) =
            oldItem == newItem
    }
}