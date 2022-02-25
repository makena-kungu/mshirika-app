package co.ke.mshirika.mshirika_app.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.remote.services.ClientsService

class ClientsPS(
    private val authKey: String,
    private val clientsService: ClientsService
) : PagingSource<Int, Client>() {

    /*private fun Response<ClientResponse>.loadResult(position: Int): LoadResult<Int, Client> {
        return try {
            val list = mutableListOf<Client>()

            body()?.let {
                list.addAll(it.pageItems)
            }

            LoadResult.Page(
                data = list,
                prevKey = if (position == STARTING_PAGING_INDEX) null else position - 1,
                nextKey = if (list.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            //network failures
            LoadResult.Error(e)
        } catch (e: HttpException) {
            //for any non 200 Http status code
            LoadResult.Error(e)
        }
    }*/

    override fun getRefreshKey(state: PagingState<Int, Client>): Int? =
        state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Client> {
        val position = params.key ?: STARTING_PAGING_INDEX
        val headers =
            mutableMapOf(
                "Fineract-Platform-TenantId" to "default",
                "Authorization" to authKey
            )
        return clientsService.clients(
            headers = headers,
            page = position,
            perPage = 10
        ).loadResult(position)
    }
}
