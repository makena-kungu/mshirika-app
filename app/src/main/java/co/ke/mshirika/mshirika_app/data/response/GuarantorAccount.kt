package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuarantorAccount(
    val accountNo: String,
    val amountPaid: Double,
    val id: Int,
    val inArrears: Boolean,
    val isActive: Boolean,
    val loanCycle: Int,
    val loanType: LoanType,
    val originalLoan: Double,
    val productId: Int,
    val productName: String,
    val shortProductName: String,
    val status: Status
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
}