package co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.TableLatestUpdated

@Dao
interface LatestUpdateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(latestUpdated: TableLatestUpdated)

    @Query("select * from tables_latest_update where table_name = :tableName")
    suspend fun get(tableName: String): TableLatestUpdated?

    @Query("delete from tables_latest_update")
    fun clear()
}