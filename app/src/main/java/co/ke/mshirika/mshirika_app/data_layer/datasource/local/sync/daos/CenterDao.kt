package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateCenter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.Center
import kotlinx.coroutines.flow.Flow

@Dao
interface CenterDao {
    @Query("select * from centers_table order by id asc")
    fun centers(): PagingSource<Int, Center>

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg centers: Center)

    @Query("delete from centers_table")
    fun clear()

    @Query("select * from create_centers_table")
    fun createCenters(): Flow<List<CreateCenter>>

    @Insert
    suspend fun insert(vararg centers: CreateCenter)
}