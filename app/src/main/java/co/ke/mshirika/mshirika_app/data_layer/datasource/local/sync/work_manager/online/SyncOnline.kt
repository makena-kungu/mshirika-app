package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.online

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncOnline @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val clients: SyncClientsOnline,
    private val loans: SyncLoansOnline,
    private val centers: SyncCentersOnline,
    private val groups: SyncGroupsOnline
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        //  upload stuff while deleting the existing values
        val clients = clients()
        val loans = loans()
        val centers = centers()
        val groups = groups()
        val v = listOf(clients, loans, centers, groups).all { it is Result.Success }
        return if (v) Result.success() else Result.failure()
    }
}