package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.offline

import androidx.work.ListenableWorker.Result
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.GroupDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.UnpackResponse.response
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject
import javax.inject.Singleton

class SyncGroupsOffline @Inject constructor(
    private val dao: GroupDao,
    private val service: GroupsService,
    private val store: PreferencesStoreRepository
) {

    private lateinit var key: String
    private val headers get() = key.headers

    private val results = mutableListOf<Result>()

    suspend operator fun invoke(): Result {
        key = store.authKey.lastOrNull() ?: return Result.failure()

        downloadGroups()
        val anErrorOccurred = results.any { it is Result.Failure }
        //if an error occurred, invalidate the whole operation
        return if (anErrorOccurred) Result.failure() else Result.success()
    }

    private suspend fun downloadGroups() {
        val result = response(
            request = {
                service.groups(headers = headers)
            },
            success = {
                val clients = it.pageItems.toTypedArray()
                dao.insert(*clients)
            }
        )

        results += result
    }
}