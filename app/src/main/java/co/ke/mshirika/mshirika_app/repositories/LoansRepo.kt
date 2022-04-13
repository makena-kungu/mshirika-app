package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import co.ke.mshirika.mshirika_app.pagingSource.LoansPagingSource
import co.ke.mshirika.mshirika_app.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.remote.services.LoansService
import co.ke.mshirika.mshirika_app.utility.Util.headers
import javax.inject.Inject

class LoansRepo @Inject constructor(
    private val service: LoansService,
    private val authKey :String,
    private val pagingSource: LoansPagingSource
) {
    val headerMap by lazy {
        headers(authKey)
    }

    val loans get() = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { pagingSource }
    ).flow

}