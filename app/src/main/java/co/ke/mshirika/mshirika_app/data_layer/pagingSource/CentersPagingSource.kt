package co.ke.mshirika.mshirika_app.data_layer.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.refreshKey
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Center
import co.ke.mshirika.mshirika_app.data_layer.remote.services.CentersService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CentersPagingSource
@Inject constructor(
    private val store: PreferencesStoreRepository,
    private val service: CentersService
) :
    PagingSource<Int, Center>() {

    override fun getRefreshKey(state: PagingState<Int, Center>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Center> {
        val position = params.key ?: STARTING_PAGING_INDEX
        val key = withContext(IO) {
            val await = async { store.authKey }
            await.await().first()
        }
        return centers(
            headers = key!!.headers,
            page = position,
            pageSize = params.loadSize
        )

    }

    private suspend fun centers(
        headers: Map<String, String>,
        page: Int,
        pageSize: Int
    ): LoadResult<Int, Center> = withContext(IO){
        service.centers(
            headers = headers,
            page = page,
            pageSize = pageSize
        ).loadResult(page, pageSize)
    }
}