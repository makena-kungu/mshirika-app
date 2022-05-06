package co.ke.mshirika.mshirika_app.data_layer.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.CentersPagingSource
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateCenter
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Center
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Search
import co.ke.mshirika.mshirika_app.data_layer.remote.response.CenterCreatedResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.services.CentersService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.empty
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CentersRepo @Inject constructor(
    private val service: CentersService,
    private val searchService: SearchService,
    private val pagingSource: CentersPagingSource,
    private val store: PreferencesStoreRepository
) {

    private val _list = mutableListOf<Center>()
    private val _searched = MutableStateFlow<PagingData<Center>>(PagingData.empty())

    val created = MutableStateFlow<Outcome<CenterCreatedResponse>>(empty())
    val offices = flow {
        withContext(IO){
            respond { service.offices(headers()) }.also {
                emit(it)
            }
        }
    }
    val searched get() = _searched.asStateFlow()

    val centers = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { pagingSource }
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

    private suspend fun headers() = withContext(IO){ store.authKey().headers }

}