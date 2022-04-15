package co.ke.mshirika.mshirika_app.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.pagingSource.Util.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.pagingSource.Util.refreshKey
import co.ke.mshirika.mshirika_app.remote.services.CentersService

class CentersPagingSource(
    private val headers: Map<String, String>,
    private val service: CentersService
) :
    PagingSource<Int, Center>() {

    override fun getRefreshKey(state: PagingState<Int, Center>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Center> {
        val position = params.key ?: STARTING_PAGING_INDEX
        return centers(
            headers = headers,
            page = position,
            pageSize = params.loadSize
        )

    }

    private suspend fun centers(
        headers: Map<String, String>,
        page: Int,
        pageSize: Int
    ): LoadResult<Int, Center> {
        return service.centers(
            headers = headers,
            page = page,
            pageSize = pageSize
        ).loadResult(page)
    }
}