package co.ke.mshirika.mshirika_app.data_layer.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.CenterDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateCenter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Search
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.Center
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.CenterCreatedResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.CentersService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.empty
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.PagingSourceUtil.pagingConfig
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.CentersRemoteMediator
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CentersRepo @Inject constructor(
    private val service: CentersService,
    private val searchService: SearchService,
    private val store: PreferencesStoreRepository,
    private val cacheDao: CacheDao,
    private val centerDao: CenterDao,
    remoteMediator: CentersRemoteMediator
) {

    private val _list = mutableListOf<Center>()
    private val _searched = MutableStateFlow<PagingData<Center>>(PagingData.empty())

    val created = MutableStateFlow<Outcome<CenterCreatedResponse>>(empty())
    val offices = channelFlow {
        withContext(IO) {
            respond { service.offices(headers()) }.also {
                send(it)
            }
        }
    }
    val searched get() = _searched.asStateFlow()

    @OptIn(ExperimentalPagingApi::class)
    val centers = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { cacheDao.centers() },
        remoteMediator = remoteMediator
    ).flow

    val centros = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { centerDao.centers() }
    ).flow

    suspend fun create(center: CreateCenter) = withContext(IO) {
        created.value = respond {
            service.create(headers(), center)
        }
    }

    suspend fun search(headers: Map<String, String>, query: String) = withContext(IO) {
        respond {
            searchService.search(
                map = headers,
                query = query,
                resource = SearchService.CENTERS
            )
        }.also {
            if (it is Success) {
                it.data?.forEach { search ->
                    search(headers, search)
                }
                _searched.value = PagingData.from(_list)
            }
        }
    }

    suspend fun search(headers: Map<String, String>, search: Search) = withContext(IO) {
        respond {
            service.center(
                headers = headers,
                accountID = search.entityId
            )
        }.also {
            if (it is Success)
                it.data?.apply {
                    _list += this
                }
        }
    }

    private suspend fun headers() = withContext(IO) { store.authKey().headers }

}