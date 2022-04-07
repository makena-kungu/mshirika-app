package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.data.response.Search
import co.ke.mshirika.mshirika_app.pagingSource.CentersPS
import co.ke.mshirika.mshirika_app.pagingSource.Util.headers
import co.ke.mshirika.mshirika_app.remote.response.CenterResponse
import co.ke.mshirika.mshirika_app.remote.services.CentersService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CentersRepo @Inject constructor(
    private val centersService: CentersService,
    private val searchService: SearchService
) {

    private val _list = mutableListOf<Center>()
    private val _searched = MutableSharedFlow<PagingData<Center>>()

    val searched: Flow<PagingData<Center>> get() = _searched.asSharedFlow()

    fun groups(authKey: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 60,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CentersPS(
                    authKey = authKey,
                    centersService = centersService
                )
            }
        ).flow

    suspend fun search(query: String, authKey: String) {
        val headers = headers(authKey)
        respond {
            searchService.search(
                map = headers,
                query = query,
                resource = arrayOf("groups")
            )
        }.also {
            if (it is Success) {
                it.data?.forEach { search ->
                    search(search, headers)
                }
            }
        }
    }

    suspend fun search(search: Search, headers: Map<String, String>) {
        respond {
            centersService.center(
                headers = headers,
                accountID = search.entityId
            )
        }.also {
            if (it is Success) {
                it.data?.let {
                    _list += it
                    _searched.tryEmit(PagingData.from(_list))
                }
            }
        }
    }

}