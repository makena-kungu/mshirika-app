package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplate

@Entity("transfer_template_table")
data class OfflineTransferFundsTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val accountId: Int,
    val template: TransferFundsTemplate
)
