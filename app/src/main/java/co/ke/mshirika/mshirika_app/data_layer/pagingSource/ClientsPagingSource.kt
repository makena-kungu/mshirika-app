package co.ke.mshirika.mshirika_app.data_layer.pagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.refreshKey
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "ClientsPagingSource"

class ClientsPagingSource
@Inject constructor(
    private val store: PreferencesStoreRepository,
    private val service: ClientsService
) : PagingSource<Int, Client>() {

    override fun getRefreshKey(state: PagingState<Int, Client>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Client> {
        val position = params.key ?: STARTING_PAGING_INDEX

        val key = withContext(IO) {
            val await = async {
                store.authKey
            }
            await.await().first()
        }
        return service.clients(
            headers = headers(key!!),
            page = position,
            perPage = params.loadSize
        ).loadResult(position)
    }
}
