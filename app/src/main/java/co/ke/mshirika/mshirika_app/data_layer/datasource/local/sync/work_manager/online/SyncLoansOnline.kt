package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.online

import androidx.work.ListenableWorker.Result
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.LoansDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class SyncLoansOnline @Inject constructor(
    private val dao: LoansDao,
    private val store: PreferencesStoreRepository,
    private val service: LoansService
) {

    private lateinit var headers: Map<String, String>

    suspend operator fun invoke(): Result {
        headers = store.headers()
        uploadLoanRepayments()
        return uploadLoans()
    }

    private suspend fun uploadLoans(): Result {
        val loans = dao.createLoans().single().toTypedArray()
        val results = mutableListOf<Result>()
        loans.forEach {
            respond { service.newLoan(headers, it) }.also {
                results += if (it !is Outcome.Success) Result.failure() else Result.success()
            }
        }

        return if (results.any { it is Result.Failure }) Result.failure()
        else Result.success()
    }

    private suspend fun uploadLoanRepayments() {
        val actions = dao.getRepayLoans()
        actions.forEach { (_, loanId, loan) ->
            service.repay(
                headers = headers,
                loanId = loanId,
                repayment = loan
            )
        }
    }
}