package co.ke.mshirika.mshirika_app.data_layer.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.GroupDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateGroup
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.GroupCreatedResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.empty
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.PagingSourceUtil.pagingConfig
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.GroupsRemoteMediator
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GroupsRepo @Inject constructor(
    private val service: GroupsService,
    private val searchService: SearchService,
    private val pref: PreferencesStoreRepository,
    private val dao: CacheDao,
    private val groupDao: GroupDao,
    remoteMediator: GroupsRemoteMediator
) {

    private val _groupCreated = MutableStateFlow<Outcome<GroupCreatedResponse>>(empty())
    private val _searched = MutableStateFlow<PagingData<Grupo>>(PagingData.empty())

    val groupCreated get() = _groupCreated.asStateFlow()
    val searched get() = _searched.asStateFlow()

    val groups = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { dao.groups() },
        remoteMediator = remoteMediator
    ).flow

    suspend fun getGroupDetails(groupId: Int) = respondWithSuccess {
        withContext(IO) {
            service.detailedGroup(headers(), groupId)
        }
    }

    val grupos = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { groupDao.groups() }
    ).flow

    suspend fun search(headers: Map<String, String>, query: String) = withContext(IO) {
        val list = mutableListOf<Grupo>()
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