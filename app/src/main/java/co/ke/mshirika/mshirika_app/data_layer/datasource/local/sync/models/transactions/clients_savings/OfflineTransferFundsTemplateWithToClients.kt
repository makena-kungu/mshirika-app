package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplateWithToClients

@Entity("transfer_funds_template_with_to_clients")
data class OfflineTransferFundsTemplateWithToClients(
    @PrimaryKey(autoGenerate = false)
    val clientId: Int,
    val accountId: Int,
    val template: TransferFundsTemplateWithToClients
)