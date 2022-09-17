package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.loans

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import com.google.gson.Gson

@Entity("offline_repay_loan")
@TypeConverters(OfflineRepayLoan.RepayLoanConverter::class)
data class OfflineRepayLoan constructor(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val loanId: Int,
    val loan: PaymentTransaction
) {
    object RepayLoanConverter {
        @TypeConverter
        fun loan(json: String): PaymentTransaction {
            return Gson().fromJson(json, PaymentTransaction::class.java)
        }

        @TypeConverter
        fun loan(loanRepayment: PaymentTransaction): String {
            return Gson().toJson(loanRepayment)
        }
    }
}
