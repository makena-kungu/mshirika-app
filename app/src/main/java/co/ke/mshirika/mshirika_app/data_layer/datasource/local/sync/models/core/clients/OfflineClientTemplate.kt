package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate

@Entity(tableName = "client_template")
data class OfflineClientTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val template: ClientTemplate
)
