package co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tables_latest_update")
data class TableLatestUpdated(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("table_name")
    val tableName: String,
    val time: Long
) {

    companion object {
        const val CLIENTES_TABLE = "clientes_table"
        const val CENTROS_TABLE = "centros_table"
        const val PRESTAMOS_TABLE = "prestamos_table"
        const val GRUPOS_TABLE = "grupos_table"
        const val TRANSACTIONS_TABLE = "transactions_table"
    }
}