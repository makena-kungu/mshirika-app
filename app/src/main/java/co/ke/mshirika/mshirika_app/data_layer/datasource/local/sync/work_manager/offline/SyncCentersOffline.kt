package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.offline

import androidx.work.ListenableWorker.Result
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.CenterDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.UnpackResponse.response
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.CentersService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject
import javax.inject.Singleton

class SyncCentersOffline @Inject constructor(
    private val dao: CenterDao,
    private val service: CentersService,
    private val store: PreferencesStoreRepository
) {

    private lateinit var key: String
    private val headers get() = key.headers

    private val results = mutableListOf<Result>()

    suspend operator fun invoke(): Result {
        key = store.authKey.lastOrNull() ?: return Result.failure()

        downloadCenters()
        val anErrorOccurred = results.any { it is Result.Failure }
        //if an error occurred, invalidate the whole operation
        return if (anErrorOccurred) Result.failure() else Result.success()
    }

    private suspend fun downloadCenters() {
        val result = response(
            request = {
                service.centers(headers)
            },
            success = {
                val clients = it.pageItems.toTypedArray()
                dao.insert(*clients)
            }
        )

        results += result
    }
}