package co.ke.mshirika.mshirika_app.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.pagingSource.Util.headers
import co.ke.mshirika.mshirika_app.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.remote.services.CentersService

class CentersPS(
    private val authKey: String,
    private val centersService: CentersService
) :
    PagingSource<Int, Center>() {

    override fun getRefreshKey(state: PagingState<Int, Center>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Center> {
        val position = params.key ?: STARTING_PAGING_INDEX
        return centers(
            headers = headers(authKey),
            page = position,
            pageSize = params.loadSize
        )
    }


    private suspend fun centers(
        headers: Map<String, String>,
        page: Int,
        pageSize: Int
    ): LoadResult<Int, Center> {
        return centersService.centers(
            headers = headers,
            page = page,
            pageSize = pageSize
        ).loadResult(page)
    }
}