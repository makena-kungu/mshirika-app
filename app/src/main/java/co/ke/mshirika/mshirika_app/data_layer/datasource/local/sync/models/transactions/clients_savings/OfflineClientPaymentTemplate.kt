package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.ClientPaymentTemplate

@Entity("offline_payment_template")
data class OfflineClientPaymentTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val clientId: Int,
    @ColumnInfo("payment_template")
    val paymentTemplate: ClientPaymentTemplate
)
