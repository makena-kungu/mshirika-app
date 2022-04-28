package co.ke.mshirika.mshirika_app.data_layer.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.refreshKey
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.data_layer.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupsPagingSource
@Inject constructor(
    private val store: PreferencesStoreRepository,
    private val service: GroupsService
) :
    PagingSource<Int, Group>() {
    override fun getRefreshKey(state: PagingState<Int, Group>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Group> {
        val position = params.key ?: STARTING_PAGING_INDEX

        val key = withContext(Dispatchers.IO) {
            val await = async {
                store.authKey
            }
            await.await().onEach {

            }.first()
        }
        return service.groups(
            headers = key!!.headers,
            page = position,
            pageSize = params.loadSize
        ).loadResult(position)
    }
}