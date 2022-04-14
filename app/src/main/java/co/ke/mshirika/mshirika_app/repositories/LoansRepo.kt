package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import co.ke.mshirika.mshirika_app.data.request.Repayment
import co.ke.mshirika.mshirika_app.data.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.pagingSource.LoansPagingSource
import co.ke.mshirika.mshirika_app.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.remote.services.LoansService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.remote.utils.empty
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LoansRepo @Inject constructor(
    private val service: LoansService,
    private val authKey: String,
    private val pagingSource: LoansPagingSource
) {
    private val _repaymentTypes = MutableStateFlow<Outcome<RepaymentResponse>>(empty())
    private val _repaymentStatus = MutableStateFlow<Outcome<RepaymentSuccessful>>(empty())

    val loans
        get() = Pager(
            config = pagingConfig(),
            pagingSourceFactory = { pagingSource }
        ).flow

    val repaymentTypes get() = _repaymentTypes.asStateFlow()
    val repaymentStatus get() = _repaymentStatus.asStateFlow()

    suspend fun repaymentTypes() {
        respond {
            service.repaymentType(authKey.headers)
        }.also {
            _repaymentTypes.value = it
        }
    }

    suspend fun repay(loanId: Int, repayment: Repayment) {
        respond {
            service.repay(authKey.headers, loanId, repayment = repayment)
        }.let {
            _repaymentStatus.value = it
        }
    }

}