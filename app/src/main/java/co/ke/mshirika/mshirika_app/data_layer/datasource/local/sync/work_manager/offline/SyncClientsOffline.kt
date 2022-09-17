package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.offline

import android.util.Log
import androidx.work.ListenableWorker.Result
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.ClientDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients.OfflineAccounts
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients.OfflineTransactions
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings.OfflineClientPaymentTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.UnpackResponse.response
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject

class SyncClientsOffline @Inject constructor(
    private val dao: ClientDao,
    private val service: ClientsService,
    private val store: PreferencesStoreRepository
) {

    private lateinit var key: String
    private val headers get() = key.headers

    private val results = mutableListOf<Result>()

    suspend operator fun invoke(): Result {
        key = store.authKey.lastOrNull() ?: return Result.failure()

        Log.d(TAG, "invoke: invoked")
        downloadClients()
        downloadClientTemplate()
        service.template(headers)
        val anErrorOccurred = results.any { it is Result.Failure }
        //if an error occurred, invalidate the whole operation
        return if (anErrorOccurred) Result.failure() else Result.success()
    }

    private suspend fun downloadClients() {
        val result = response(
            request = {
                service.clients(headers)
            },
            success = {
                val clients = it.pageItems.toTypedArray()
                dao.insert(*clients)
                clients.forEach { client ->
                    downloadClientAccounts(clientId = client.id)
                    downloadCreateClientTemplate(client)
                    downloadTransactions(client.savingsAccountId)
                }
            }
        )

        results += result
    }

    private suspend fun downloadClientTemplate() {
        results += response(
            request = { service.template(headers) },
            success = {
                store.saveClientTemplate(it)
            }
        )
    }

    private suspend fun downloadClientAccounts(clientId: Int) {
        response(
            request = { service.accounts(headers, clientId = clientId) }
        ) {
            val accounts = OfflineAccounts(id = 0, clientId = clientId, accounts = it)
            dao.insert(accounts)
        }
    }

    private suspend fun downloadCreateClientTemplate(client: Cliente) {
        val clientId = client.id
        response(
            request = {
                service.templateClientPayment(
                    headers = headers,
                    clientId = clientId
                )
            },
            success = {
                val template = OfflineClientPaymentTemplate(
                    id = 0,
                    paymentTemplate = it,
                    clientId = clientId
                )
                dao.insert(template)
            }
        )
    }

    private suspend fun downloadTransactions(accountId: Int) {
        results += response(
            request = { service.transactions(headers, accountId) }
        ) {
            val transactions = it.transactions?: emptyList()
            val transaction = OfflineTransactions(
                id = 0,
                accountId = accountId,
                transactions = transactions
            )
            dao.insert(transaction)
        }
    }
    
    companion object {
        private const val TAG = "SyncClientsOffline"
    }
}