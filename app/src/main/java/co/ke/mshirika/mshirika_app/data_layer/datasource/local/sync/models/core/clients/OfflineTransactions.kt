package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Transaction

@Entity(tableName = "transactions")
data class OfflineTransactions(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val accountId: Int,
    val transactions: List<Transaction>
)
