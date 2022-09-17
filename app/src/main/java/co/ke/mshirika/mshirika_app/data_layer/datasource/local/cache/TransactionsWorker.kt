package co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TransactionsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val dao: CacheDao,
    private val service: ClientsService,
    private val store: PreferencesStoreRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        requestTransactions()
        return Result.success()
    }

    private suspend fun requestTransactions() {
        val headers = headers()
        respondWithSuccess {
            service.clients(headers = headers, calledBy = "transactions")
        }.let { feedback ->
            if (feedback == null) return@let

            feedback.pageItems.forEach {
                respondWithSuccess {
                    service.transactions(
                        headers = headers,
                        accountId = it.savingsAccountId
                    )
                }?.apply {
                    transactions?.forEach {
                        dao.insert(it)
                    }
                }
            }
        }
    }

    private suspend fun headers() = store.headers()

    companion object {
        private const val TAG = "TransactionsWorker"
    }
}