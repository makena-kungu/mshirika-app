package co.ke.mshirika.mshirika_app.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.pagingSource.Util.headers
import co.ke.mshirika.mshirika_app.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.remote.services.GroupsService

class GroupsPS(private val authKey: String, private val groupsService: GroupsService) :
    PagingSource<Int, Group>() {
    override fun getRefreshKey(state: PagingState<Int, Group>): Int?  =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Group> {
        val position = params.key ?: STARTING_PAGING_INDEX

        return groupsService.groups(
            headers = headers(authKey),
            page = position,
            pageSize = params.loadSize
        ).loadResult(position)
    }
}