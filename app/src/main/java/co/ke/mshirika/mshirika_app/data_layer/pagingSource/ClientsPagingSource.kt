package co.ke.mshirika.mshirika_app.data_layer.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.PagingSourceUtil.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.PagingSourceUtil.loadResult
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.PagingSourceUtil.refreshKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.SearchService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ClientsPagingSource @JvmOverloads constructor(
    private val store: PreferencesStoreRepository,
    private val service: SearchService,
    var query: String? = null
) : PagingSource<Int, Cliente>() {

    override fun getRefreshKey(state: PagingState<Int, Cliente>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cliente> {
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
