package co.ke.mshirika.mshirika_app.data_layer.remote.models.response

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.common.Currency
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class Transaction(
    val accountId: Int,
    val accountNo: String,
    val amount: Double,
    val currency: Currency,
    val date: List<Int>,
    val id: Int,
    val interestedPostedAsOn: Boolean,
    val paymentDetailData: PaymentDetailData,
    val reversed: Boolean,
    val runningBalance: Double,
    val submittedByUsername: String,
    val submittedOnDate: List<Int>,
    val transactionType: TransactionType,
    val transfer: Transfer
) : Parcelable {

    @Parcelize
    @Keep
    data class PaymentDetailData(
        val accountNumber: String,
        val bankNumber: String,
        val checkNumber: String,
        val id: Int,
        val paymentType: PaymentType,
        val receiptNumber: String,
        val routingCode: String,
        val transactionDate: String
    ) : Parcelable {

        @Parcelize
        @Keep
        data class PaymentType(
            val id: Int,
            val name: String
        ) : Parcelable
    }

    @Parcelize
    @Keep
    data class TransactionType(
        val amountHold: Boolean,
        val amountRelease: Boolean,
        val approveTransfer: Boolean,
        val code: String,
        val deposit: Boolean,
        val dividendPayout: Boolean,
        val escheat: Boolean,
        val feeDeduction: Boolean,
        val id: Int,
        val initiateTransfer: Boolean,
        val interestPosting: Boolean,
        val overdraftFee: Boolean,
        val overdraftInterest: Boolean,
        val rejectTransfer: Boolean,
        val value: String,
        val withdrawTransfer: Boolean,
        val withdrawal: Boolean,
        val withholdTax: Boolean,
        val writtenoff: Boolean
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

    companion object : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}