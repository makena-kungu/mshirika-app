package co.ke.mshirika.mshirika_app.data_layer.repositories.loans

import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.LoansPagingSource
import co.ke.mshirika.mshirika_app.data_layer.pagingSource.Util
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateGuarantor
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Repayment
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.*
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.GuarantorsTemplateWithClient
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoansRepoImpl @Inject constructor(
    private val service: LoansService,
    private val store: PreferencesStoreRepository,
    private val pagingSource: LoansPagingSource
) : LoansRepo {

    override val loans: Flow<PagingData<ConservativeLoanAccount>> = Pager(
        config = Util.pagingConfig(pageSize = 10),
        pagingSourceFactory = { pagingSource }
    ).flow

    override suspend fun createGuarantor(
        loanId: Int,
        createGuarantor: CreateGuarantor
    ): Outcome<CreateGuarantorResponse> = withContext(
        IO
    ) {
        UnpackResponse.respond {
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

    override suspend fun guarantorsTemplate(
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
            UnpackResponse.respond {
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

    override suspend fun repay(loanId: Int, repayment: Repayment): RepaymentSuccessful? =
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