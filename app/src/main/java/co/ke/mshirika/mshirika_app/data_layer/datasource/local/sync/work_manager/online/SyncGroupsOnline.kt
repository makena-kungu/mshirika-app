package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.online

import androidx.work.ListenableWorker.Result
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.GroupDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateGroup
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class SyncGroupsOnline @Inject constructor(
    private val dao: GroupDao,
    private val store: PreferencesStoreRepository,
    private val service: GroupsService
) {

    private lateinit var headers: Map<String, String>

    suspend operator fun invoke(): Result {
        headers = store.headers()
        val groups = dao.createGroups().single().toTypedArray()
        return uploadGroups(*groups)
    }

    private suspend fun uploadGroups(vararg groups: CreateGroup): Result {
        val results = mutableListOf<Result>()
        groups.forEach {
            respond { service.create(headers, it) }.also {
                results += if (it !is Outcome.Success) Result.failure() else Result.success()
            }
        }

        return if (results.any { it is Result.Failure }) Result.failure()
        else Result.success()
    }
}