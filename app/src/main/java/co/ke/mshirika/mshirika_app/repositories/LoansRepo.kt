package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.request.Repayment
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.pagingSource.LoansPagingSource
import co.ke.mshirika.mshirika_app.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.remote.services.LoansService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.remote.utils.empty
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoansRepo @Inject constructor(
    private val service: LoansService,
    private val prefRepo: PreferencesStoreRepository
) {
    private val _repaymentTypes = MutableStateFlow<Outcome<RepaymentResponse>>(empty())
    private val _repaymentStatus = MutableStateFlow<Outcome<RepaymentSuccessful>>(empty())

    val loans: Flow<PagingData<LoanAccount>>
        get() = flow {
            val authKey = prefRepo.authKey()
            Pager(
                config = pagingConfig(),
                pagingSourceFactory = { LoansPagingSource(authKey, service) }
            ).flow
        }

    val repaymentTypes get() = _repaymentTypes.asStateFlow()
    val repaymentStatus get() = _repaymentStatus.asStateFlow()

    suspend fun repaymentTypes() {
        respond {
            service.repaymentType(prefRepo.authKey().headers)
        }.also {
            _repaymentTypes.value = it
        }
    }

    suspend fun repay(loanId: Int, repayment: Repayment) {
        respond {
            service.repay(
                headers = prefRepo.authKey().headers,
                loanId = loanId,
                repayment = repayment
            )
        }.let {
            _repaymentStatus.value = it
        }
    }

}