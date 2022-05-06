package co.ke.mshirika.mshirika_app.data_layer.repositories

import androidx.paging.Pager
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.LoansPagingSource
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Repayment
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.data_layer.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.empty
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoansRepo @Inject constructor(
    private val service: LoansService,
    private val store: PreferencesStoreRepository,
    private val pagingSource: LoansPagingSource
) {
    private val _repaymentTypes = MutableStateFlow<Outcome<RepaymentResponse>>(empty())
    private val _repaymentStatus = MutableStateFlow<Outcome<RepaymentSuccessful>>(empty())

    val loans = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { pagingSource }
    ).flow

    val repaymentTypes get() = _repaymentTypes.asStateFlow()
    val repaymentStatus get() = _repaymentStatus.asStateFlow()

    suspend fun getLoanTemplate(clientId: Int) = withContext(IO) {
        respondWithSuccess {
            service.template1(
                headers = headers(),
                clientId = clientId
            )
        }
    }

    suspend fun getLoanTemplate(clientId: Int, productId: Int) = withContext(IO) {
        respondWithSuccess {
            service.template2(
                headers = headers(),
                clientId = clientId,
                productId = productId
            )
        }
    }

    suspend fun repaymentTypes() = withContext(IO) {
        respond {
            service.repaymentType(store.authKey().headers)
        }.also {
            _repaymentTypes.value = it
        }
    }
    suspend fun repay(loanId: Int, repayment: Repayment) = withContext(IO) {
        respond {
            service.repay(
                headers = store.authKey().headers,
                loanId = loanId,
                repayment = repayment
            )
        }
    }

    suspend fun headers() = withContext(IO) {
        store.authKey.first()!!.headers
    }

}