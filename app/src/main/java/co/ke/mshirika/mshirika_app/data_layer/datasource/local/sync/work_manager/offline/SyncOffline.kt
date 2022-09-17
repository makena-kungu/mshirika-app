package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.offline

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.failure
import androidx.work.ListenableWorker.Result.success
import androidx.work.WorkerParameters
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncOffline @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val clients: SyncClientsOffline,
    private val groups: SyncGroupsOffline,
    private val centers: SyncCentersOffline,
    private val loans: SyncLoansOffline
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val clients = clients()
        val centers = centers()
        val loans = loans()
        val groups = groups()

        val result = listOf(clients, centers, loans, groups)
            .all { it is Result.Success }
        return if (result) success() else failure()
    }
}