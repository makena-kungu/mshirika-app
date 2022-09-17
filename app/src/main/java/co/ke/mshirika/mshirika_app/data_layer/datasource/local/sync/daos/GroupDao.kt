package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateGroup
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("select * from groups_table order by id asc")
    fun groups(): PagingSource<Int, Grupo>

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg groups: Grupo)

    @Insert
    suspend fun insert(vararg groups: CreateGroup)

    @Query("delete from groups_table")
    fun clear()

    @Query("select * from create_group_table")
    fun createGroups():Flow<List<CreateGroup>>
}