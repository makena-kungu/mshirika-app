package co.ke.mshirika.mshirika_app.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.pagingSource.Util.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.pagingSource.Util.refreshKey
import co.ke.mshirika.mshirika_app.remote.services.GroupsService

class GroupsPagingSource(
    private val headers: Map<String, String>,
    private val service: GroupsService
) :
    PagingSource<Int, Group>() {
    override fun getRefreshKey(state: PagingState<Int, Group>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Group> {
        val position = params.key ?: STARTING_PAGING_INDEX

        return service.groups(
            headers = headers,
            page = position,
            pageSize = params.loadSize
        ).loadResult(position)
    }
}