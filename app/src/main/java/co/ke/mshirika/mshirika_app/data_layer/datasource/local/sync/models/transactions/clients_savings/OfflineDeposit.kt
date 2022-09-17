package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings.OfflineDeposit.OfflineDepositConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import com.google.gson.Gson

@TypeConverters(OfflineDepositConverter::class)
@Entity("deposit_table")
data class OfflineDeposit constructor(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val savingsAccountId: Int,
    val deposit: PaymentTransaction
) {
    constructor(savingsAccountId: Int, deposit: PaymentTransaction) : this(
        0,
        savingsAccountId,
        deposit
    )

    object OfflineDepositConverter {
        @TypeConverter
        fun deposit(json: String): PaymentTransaction {
            return Gson().fromJson(json, PaymentTransaction::class.java)
        }

        @TypeConverter
        fun deposit(deposit: PaymentTransaction): String {
            return Gson().toJson(deposit)
        }
    }
}
