package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class Loan(
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
) : Parcelable, Respondent {

    @Parcelize
    @Keep
    data class LoanType(
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

    companion object : DiffUtil.ItemCallback<Pair<Loan, LoanRepaymentSchedule>>() {
        override fun areItemsTheSame(
            oldItem: Pair<Loan, LoanRepaymentSchedule>,
            newItem: Pair<Loan, LoanRepaymentSchedule>
        ): Boolean = oldItem.first.id == newItem.first.id
                && oldItem.second.id == newItem.second.id

        override fun areContentsTheSame(
            oldItem: Pair<Loan, LoanRepaymentSchedule>,
            newItem: Pair<Loan, LoanRepaymentSchedule>
        ): Boolean {
            return oldItem.second == newItem.second && newItem.first == oldItem.first
        }
    }
}