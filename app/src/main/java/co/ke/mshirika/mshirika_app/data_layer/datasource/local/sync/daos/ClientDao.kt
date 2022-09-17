package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients.OfflineAccounts
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients.OfflineTransactions
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.TransferFunds
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplateWithToClients
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplateWithToClientsAccounts
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.ClientPaymentTemplate
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language

@Dao
interface ClientDao {
    @Query("select * from clients_table")
    fun clients(): PagingSource<Int, Cliente>

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg clients: Cliente)

    @Query("delete from clients_table")
    fun clear()

    @Query("select * from create_clients_table")
    fun createClients(): Flow<List<CreateClient>>

    @Insert
    suspend fun insert(vararg clients: CreateClient)

    @Language("RoomSql")
    @Query("select payment_template from offline_payment_template where clientId =:clientId")
    suspend fun clientPaymentTemplate(clientId: Int): ClientPaymentTemplate?

    @Insert
    fun insert(vararg templates: OfflineClientPaymentTemplate)

    @Query("select * from transactions where accountId = :accountId")
    suspend fun transactions(accountId: Int): OfflineTransactions?

    @Insert
    suspend fun insert(vararg transactions: OfflineTransactions)

    @Query("select accounts from offline_accounts where clientId = :clientId")
    suspend fun accounts(clientId: Int): AccountsResponse

    @Insert
    suspend fun insert(vararg accounts: OfflineAccounts)

    @Insert
    suspend fun insert(vararg transferFundsTemplate: OfflineTransferFundsTemplate)

    @Query("select template from transfer_template_table where accountId = :fromAccountId")
    suspend fun getOfflineTransferFundsTemplate(fromAccountId: Int): TransferFundsTemplate?

    @Insert
    suspend fun insert(vararg templates: OfflineTransferFundsTemplateWithToClients)

    @Query("select template from transfer_funds_template_with_to_clients where accountId = :fromAccountId")
    suspend fun getOfflineTransferFundsWithToClientsTemplate(fromAccountId: Int): TransferFundsTemplateWithToClients?

    @Insert
    suspend fun insert(vararg templates: OfflineTransferFundsTemplateWithToClientsAccounts)

    @Query("select template from transfer_funds_with_to_clients_accounts_template where accountId = :fromAccountId")
    suspend fun getOfflineTransferFundsTemplateWithToClientsAccounts(fromAccountId: Int): TransferFundsTemplateWithToClientsAccounts?

    @Insert
    fun insert(vararg transfer: TransferFunds)

    @Query("select * from deposits_table")
    fun getDeposits(): Flow<List<Deposit>>

    @Insert
    suspend fun insert(vararg deposits: Deposit)

    @Insert
    suspend fun insert(vararg charge: OfflineCharge)

    @Query("select * from charges_table")
    suspend fun getCharges(): List<OfflineCharge>

    @Insert
    suspend fun insert(offlineDeposit: OfflineDeposit)

    @Query("select * from deposit_table")
    suspend fun getDepositTransactions(): List<OfflineDeposit>
}