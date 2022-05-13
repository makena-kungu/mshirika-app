package co.ke.mshirika.mshirika_app.data_layer.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.GroupsPagingSource
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateGroup
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.data_layer.remote.response.GroupCreatedResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.response.OfficeResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.empty
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupsRepo @Inject constructor(
    private val service: GroupsService,
    private val searchService: SearchService,
    private val pref: PreferencesStoreRepository,
    private val pagingSource: GroupsPagingSource
) {

    private val _groupCreated = MutableStateFlow<Outcome<GroupCreatedResponse>>(empty())
    private val _searched = MutableStateFlow<PagingData<Group>>(PagingData.empty())

    val groupCreated get() = _groupCreated.asStateFlow()
    val searched get() = _searched.asStateFlow()

    val groups = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { pagingSource }
    ).flow

    suspend fun search(headers: Map<String, String>, query: String) = withContext(IO) {
        val list = mutableListOf<Group>()
        respondWithSuccess {
            searchService.search(
                map = headers,
                query = query,
                resource = arrayOf("groups")
            )
        }?.onEach {
            respondWithSuccess {
                service.group(
                    headers = headers,
                    accountID = it.entityId
                )
            }?.let {
                list += it
                _searched.tryEmit(PagingData.from(list))
            }
        }
    }

    suspend fun offices() = withContext(IO) {
        respondWithSuccess {
            service.offices(headers())
        }
    }

    suspend fun create(group: CreateGroup) = withContext(IO) {
        respond {
            service.create(
                headers(),
                group
            )
        }.also { _groupCreated.value = it }
    }

    private suspend fun headers() = withContext(IO) { pref.authKey().headers }
}