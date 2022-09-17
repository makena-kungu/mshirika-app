package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.online

import androidx.work.ListenableWorker.Result
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.ClientDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class SyncClientsOnline @Inject constructor(
    private val dao: ClientDao,
    private val store: PreferencesStoreRepository,
    private val service: ClientsService
) {

    private lateinit var headers: Map<String, String>

    suspend operator fun invoke(): Result {
        headers = store.headers()
        uploadClients()
        uploadDeposits()
        uploadCharges()
        return Result.success()
    }

    private suspend fun uploadClients(): Result {
        val clients = dao.createClients().single().toTypedArray()
        val results = mutableListOf<Result>()
        clients.forEach {
            respond { service.create(headers, it) }.also {
                results += if (it !is Outcome.Success) Result.failure() else Result.success()
            }
        }

        return if (results.any { it is Result.Failure }) Result.failure()
        else Result.success()
    }

    private suspend fun uploadDeposits() {
        val transactions = dao.getDepositTransactions()
        transactions.forEach { (_, savingsAccountId, deposit) ->
            service.deposit(
                headers = headers,
                savingsAccountId = savingsAccountId,
                deposit = deposit
            )
        }
    }

    private suspend fun uploadCharges() {
        val charges = dao.getCharges()
        charges.forEach { (_, savingsAccountId, chargeId, charge) ->
            service.charge(headers, savingsAccountId, chargeId, charge)
        }
    }
}