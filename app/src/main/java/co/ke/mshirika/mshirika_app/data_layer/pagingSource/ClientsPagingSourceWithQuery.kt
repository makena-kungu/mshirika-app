package co.ke.mshirika.mshirika_app.data_layer.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.refreshKey
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ClientsPagingSourceWithQuery @JvmOverloads constructor(
    private val store: PreferencesStoreRepository,
    private val service: SearchService,
    var query: String? = null
) : PagingSource<Int, Client>() {

    override fun getRefreshKey(state: PagingState<Int, Client>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Client> {
        val position = params.key ?: STARTING_PAGING_INDEX
        return withContext(IO) {
            val await = async { store.authKey }
            val key = await.await().first()

            if (query.isNullOrEmpty()) return@withContext loadResult()
            service.searchClient(
                headers = headers(key!!),
                page = position,
                perPage = params.loadSize,
                sqlSearch = SearchService.clients(query ?: "")
            ).loadResult(position, params.loadSize)
        }
    }

    companion object {
        private const val TAG = "ClientsPagingSource"
    }
}
