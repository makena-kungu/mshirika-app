package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerApprove
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerDelete
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerInboxResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerTask
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.CheckerService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.empty
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.loading
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CheckerRepo @Inject constructor(
    private val service: CheckerService,
    private val store: PreferencesStoreRepository
) {
    private val _updater = MutableStateFlow<Outcome<CheckerInboxResponse>>(empty())
    val updater: MutableStateFlow<Outcome<CheckerInboxResponse>>
        get() = _updater

    suspend fun list() {
        _updater.value = loading()
        withContext(IO) { _updater.value = respond { service.checkerInbox(store.headers()) } }
    }

    suspend fun approve(task: CheckerTask): Outcome<CheckerApprove> {
        val result = withContext(IO) {
            respond { service.approve(store.headers(), task.id) }
        }
        list()
        return result
    }

    suspend fun reject(task: CheckerTask): Outcome<CheckerApprove> {
        val result = withContext(IO) {
            respond { service.reject(store.headers(), task.id) }
        }
        list()
        return result
    }

    suspend fun delete(task: CheckerTask): Outcome<CheckerDelete> {
        val result = withContext(IO) {
            respond {
                service.delete(store.headers(), task.id)
            }
        }
        list()
        return result
    }
}