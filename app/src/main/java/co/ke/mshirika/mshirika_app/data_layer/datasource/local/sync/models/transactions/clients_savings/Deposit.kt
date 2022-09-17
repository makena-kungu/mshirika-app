package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import com.google.gson.Gson

@Entity("deposits_table")
data class Deposit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val savingsAccountId: Int,
    val shares: PaymentTransaction
) {
    object DepositsConverter {
        @TypeConverter
        fun shares(shares: PaymentTransaction): String {
            return Gson().toJson(shares)
        }

        @TypeConverter
        fun shares(json: String): PaymentTransaction {
            return Gson().fromJson(json, PaymentTransaction::class.java)
        }
    }
}