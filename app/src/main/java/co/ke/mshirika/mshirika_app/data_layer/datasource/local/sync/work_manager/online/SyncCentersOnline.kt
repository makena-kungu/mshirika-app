package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.online

import androidx.work.ListenableWorker.Result
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.CenterDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateCenter
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.CentersService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class SyncCentersOnline @Inject constructor(
    private val dao: CenterDao,
    private val store: PreferencesStoreRepository,
    private val service: CentersService
) {

    private lateinit var headers: Map<String, String>

    suspend operator fun invoke(): Result {
        headers = store.headers()
        val centers = dao.createCenters().single().toTypedArray()
        return uploadCenters(*centers)
    }

    private suspend fun uploadCenters(vararg centers: CreateCenter): Result {
        val results = mutableListOf<Result>()
        centers.forEach {
            respond { service.create(headers, it) }.also {
                results += if (it !is Outcome.Success) Result.failure() else Result.success()
            }
        }

        return if (results.any { it is Result.Failure }) Result.failure()
        else Result.success()
    }
}