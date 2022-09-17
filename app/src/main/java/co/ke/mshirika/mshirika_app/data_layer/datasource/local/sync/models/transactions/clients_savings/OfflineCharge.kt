package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import com.google.gson.Gson

@Entity("charges_table")
@TypeConverters(OfflineCharge.ChargeConverter::class)
data class OfflineCharge constructor(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val savingsAccountId: Int,
    val chargeId: Int,
    val charge: PaymentTransaction
) {
    object ChargeConverter {

        @TypeConverter
        fun charge(json: String): PaymentTransaction {
            return Gson().fromJson(json, PaymentTransaction::class.java)
        }

        @TypeConverter
        fun charge(charge: PaymentTransaction): String {
            return Gson().toJson(charge)
        }
    }
}
