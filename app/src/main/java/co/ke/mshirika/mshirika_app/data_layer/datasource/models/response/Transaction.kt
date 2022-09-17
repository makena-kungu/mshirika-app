package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.common.Currency
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Keep
@Entity(tableName = "transactions_table")
@TypeConverters(Transaction.Converter::class)
@Parcelize
data class Transaction(
    @PrimaryKey
    val id: Int,
    val accountId: Int,
    val accountNo: String,
    val amount: Double,
    val currency: Currency,
    val date: List<Int>,
    val interestedPostedAsOn: Boolean,
    val paymentDetailData: PaymentDetailData?,
    val reversed: Boolean,
    val runningBalance: Double,
    val submittedByUsername: String?,
    val submittedOnDate: List<Int>,
    val transactionType: TransactionType,
    val transfer: Transfer?
) : Parcelable {
    @Keep
    @Parcelize
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
        @Keep
        @Parcelize
        data class PaymentType(
            val id: Int,
            val name: String
        ) : Parcelable
    }

    @Keep
    @Parcelize
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

    @Keep
    @Parcelize
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


    internal object Converter {
        @TypeConverter
        @JvmStatic
        fun fromPaymentDetailData(obj: PaymentDetailData?): String {
            return Gson().toJson(obj)
        }

        @TypeConverter
        @JvmStatic
        fun toPaymentDetailData(json: String): PaymentDetailData? {
            return Gson().fromJson(json, PaymentDetailData::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromTransactionType(obj: TransactionType): String {
            return Gson().toJson(obj)
        }

        @TypeConverter
        @JvmStatic
        fun toTransactionType(json: String): TransactionType {
            return Gson().fromJson(json, TransactionType::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromTransfer(obj: Transfer?): String {
            return Gson().toJson(obj)
        }

        @TypeConverter
        @JvmStatic
        fun toTransfer(json: String): Transfer? {
            return Gson().fromJson(json, Transfer::class.java)
        }
    }
}