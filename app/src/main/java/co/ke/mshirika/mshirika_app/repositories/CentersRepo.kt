package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.data.response.Search
import co.ke.mshirika.mshirika_app.pagingSource.CentersPagingSource
import co.ke.mshirika.mshirika_app.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.remote.services.CentersService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CentersRepo @Inject constructor(
    private val service: CentersService,
    private val searchService: SearchService,
    private val pref: PreferencesStoreRepository
) {

    private val _list = mutableListOf<Center>()
    private val _searched = MutableStateFlow<PagingData<Center>>(PagingData.empty())

    val searched get() = _searched.asStateFlow()

    val centers: Flow<PagingData<Center>>
        get() {
            return flow {
                val headers = pref.authKey().headers
                Pager(
                    config = pagingConfig(),
                    pagingSourceFactory = { CentersPagingSource(headers, service) }
                ).flow
            }
        }

    suspend fun search(headers: Map<String, String>, query: String) {
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

    suspend fun search(headers: Map<String, String>, search: Search) {
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

}