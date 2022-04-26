package co.ke.mshirika.mshirika_app.data_layer.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.refreshKey
import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import co.ke.mshirika.mshirika_app.utility.Util.headers

class LoansPagingSource(
    private val authKey: String,
    private val service: LoansService
) :
    PagingSource<Int, LoanAccount>() {

    override fun getRefreshKey(state: PagingState<Int, LoanAccount>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LoanAccount> {
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
    ): LoadResult<Int, LoanAccount> {
        return service.loan(
            headers = headers,
            page = page,
            perPage = pageSize
        ).loadResult(page)
    }
}