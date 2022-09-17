package co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos

import androidx.paging.PagingSource
import androidx.room.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Office
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Transaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.Center
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language

@Dao
interface CacheDao {

    @Language("RoomSql")
    @Query("SELECT * from clients_table")
    fun clients(): PagingSource<Int, Cliente>

    @Query("select count(*) from clients_table")
    suspend fun clientCount(): Int

    @Query("select count(*) from groups_table")
    suspend fun groupsCount(): Int

    @Query("select count(*) from loans_table")
    suspend fun loansCount(): Int

    @Query("select count(*) from centers_table")
    suspend fun centersCount(): Int

    @Query("select * from groups_table order by name")
    fun groups(): PagingSource<Int, Grupo>

    @Query("select * from centers_table order by name")
    fun centers(): PagingSource<Int, Center>

    @Query("select * from client_template")
    fun clientTemplates(): Flow<List<ClientTemplate>>

    @Query("select * from loans_table")
    fun loans(): PagingSource<Int, ConservativeLoanAccount>

    @Query("select * from loans_table")
    fun prestamos(): Flow<List<ConservativeLoanAccount>>

    @Query("select * from office_table")
    fun offices(): Flow<List<Office>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg clients: Cliente): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg groups: Grupo)

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insert(loan: ConservativeLoanAccount):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoans(loans: List<ConservativeLoanAccount>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg centers: Center)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(array: List<Cliente>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg clientTemplates: ClientTemplate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg offices: Office)

    @Delete
    suspend fun delete(vararg clients: Cliente)

    @Delete
    suspend fun delete(vararg groups: Grupo)

    @Delete
    suspend fun delete(vararg centers: Center)

    @Delete
    suspend fun delete(vararg clientTemplates: ClientTemplate)

    @Delete
    suspend fun delete(vararg offices: Office)

    @Delete
    suspend fun delete(vararg transactions: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg transactions: Transaction)

    @Query("select count(*) from transactions_table")
    suspend fun transactionsCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<Transaction>)

    @Query("select * from transactions_table")
    fun transactions(): Flow<List<Transaction>>

    @Query("delete from clients_table")
    suspend fun clearClients()

    @Query("delete from groups_table")
    suspend fun clearGroups()

    @Query("delete from centers_table")
    suspend fun clearCenters()

    @Query("delete from loans_table")
    suspend fun clearLoans()

    @Query("delete from client_template")
    suspend fun clearClientTemplates()

    @Query("delete from transactions_table")
    suspend fun clearTransactions()

    @Query("delete from office_table")
    fun clearOffices()

    companion object {
        private const val TAG = "CacheDao"
    }
}
