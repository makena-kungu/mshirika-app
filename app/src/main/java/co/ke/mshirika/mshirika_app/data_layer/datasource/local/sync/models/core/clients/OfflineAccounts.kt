package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.AccountsResponse

@Entity("offline_accounts")
data class OfflineAccounts(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val clientId: Int,
    val accounts: AccountsResponse
)
