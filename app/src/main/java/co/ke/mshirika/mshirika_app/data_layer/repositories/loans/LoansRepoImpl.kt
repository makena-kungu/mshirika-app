package co.ke.mshirika.mshirika_app.data_layer.repositories.loans

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.filter
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateGuarantor
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.guarantors.GuarantorsTemplateWithClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.PagingSourceUtil.pagingConfig
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.LoansRemoteMediator
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class LoansRepoImpl @Inject constructor(
    private val service: LoansService,
    private val store: PreferencesStoreRepository,
    remoteMediator: LoansRemoteMediator,
    private val dao: CacheDao
) : LoansRepo {

    override val loans: Flow<PagingData<ConservativeLoanAccount>> = Pager(
        config = pagingConfig(pageSize = 10),
        pagingSourceFactory = { dao.loans() },
        remoteMediator = remoteMediator
    ).flow

    override val prestamos: Flow<List<ConservativeLoanAccount>>
        get() = dao.prestamos()

    override suspend fun createGuarantor(
        loanId: Int,
        createGuarantor: CreateGuarantor
    ): Outcome<CreateGuarantorResponse> = withContext(
        IO
    ) {
        respond {
            service.createGuarantor(headers(), loanId, createGuarantor)
        }
    }

    override suspend fun detailedLoanAccount(loanId: Int): DetailedLoanAccount? =
        withContext(IO) {
            respondWithSuccess {
                service.detailedLoan(
                    headers = headers(),
                    loanId = loanId
                )
            }
        }

    override suspend fun getLoanTemplate(clientId: Int): NewLoanTemplate? =
        withContext(IO) {
            respondWithSuccess {
                service.template1(
                    headers = headers(),
                    clientId = clientId
                )
            }
        }

    override suspend fun getLoanTemplate(clientId: Int, productId: Int): NewLoanTemplate2? =
        withContext(IO) {
            respondWithSuccess {
                service.template2(
                    headers = headers(),
                    clientId = clientId,
                    productId = productId
                )
            }
        }

    override suspend fun guarantorsTemplate(loanId: Int): LoanWithGuarantors? =
        withContext(IO) {
            respondWithSuccess {
                service.getGuarantorsTemplate(headers(), loanId)
            }
        }

    override suspend fun
            guarantorsTemplate(
        clientId: Int,
        loanId: Int
    ): GuarantorsTemplateWithClient? = withContext(IO) {
        respondWithSuccess {
            service.getGuarantorsTemplate(
                headers(),
                loanId = loanId,
                clientId = clientId
            )
        }
    }

    override suspend fun newLoan(newLoan: NewLoan): Outcome<CreateLoan> =
        withContext(IO) {
            respond {
                service.newLoan(
                    headers = headers(),
                    newLoan = newLoan
                )
            }
        }

    override suspend fun repaymentSchedule(loanId: Int): LoanRepaymentSchedule? = withContext(IO) {
        respondWithSuccess {
            service.loanRepaymentSchedule(
                headers(),
                loanId
            )
        }
    }

    override suspend fun repaymentTypes(): RepaymentResponse? = withContext(IO) {
        respondWithSuccess {
            service.repaymentType(store.authKey().headers)
        }
    }

    override suspend fun repay(loanId: Int, repayment: PaymentTransaction): RepaymentSuccessful? =
        withContext(IO) {
            respondWithSuccess {
                service.repay(
                    headers = store.authKey().headers,
                    loanId = loanId,
                    repayment = repayment
                )
            }
        }

    override suspend fun headers(): Map<String, String> = withContext(IO) {
        store.authKey.first()!!.headers
    }

}