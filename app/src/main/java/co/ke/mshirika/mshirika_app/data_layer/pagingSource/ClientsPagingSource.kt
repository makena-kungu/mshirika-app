package co.ke.mshirika.mshirika_app.data_layer.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.refreshKey
import co.ke.mshirika.mshirika_app.data_layer.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.utility.Util.headers

class ClientsPagingSource(
    private val authKey:String,
    private val service: ClientsService
) : PagingSource<Int, Client>() {

    override fun getRefreshKey(state: PagingState<Int, Client>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Client> {
        val position = params.key ?: STARTING_PAGING_INDEX

        return service.clients(
                headers = headers(authKey),
                page = position,
                perPage = 10
            ).loadResult(position)
    }
}
