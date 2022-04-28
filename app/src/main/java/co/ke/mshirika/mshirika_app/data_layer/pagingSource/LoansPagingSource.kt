package co.ke.mshirika.mshirika_app.data_layer.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.STARTING_PAGING_INDEX
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.loadResult
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.refreshKey
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoansPagingSource
@Inject constructor(
    private val store: PreferencesStoreRepository,
    private val service: LoansService
) :
    PagingSource<Int, LoanAccount>() {

    override fun getRefreshKey(state: PagingState<Int, LoanAccount>): Int? =
        state.refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LoanAccount> {
        val position = params.key ?: STARTING_PAGING_INDEX

        val key = withContext(Dispatchers.IO) {
            val await = async {
                store.authKey
            }
            await.await().first()
        }
        return loans(
            headers = headers(key!!),
            page = position,
            pageSize = params.loadSize
        )
    }


    private suspend fun loans(
        headers: Map<String, String>,
        page: Int,
        pageSize: Int
    ): LoadResult<Int, LoanAccount> = service.loan(
        headers = headers,
        page = page,
        perPage = pageSize
    ).loadResult(page)
}