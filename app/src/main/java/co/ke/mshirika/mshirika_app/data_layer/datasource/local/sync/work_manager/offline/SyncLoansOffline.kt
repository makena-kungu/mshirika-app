package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.offline

import androidx.work.ListenableWorker.Result
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.LoansDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.loans.OfflineDetailedLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager.UnpackResponse.response
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SyncLoansOffline @Inject constructor(
    private val service: LoansService,
    private val store: PreferencesStoreRepository,
    private val loansDao: LoansDao
) {

    private lateinit var headers: Map<String, String>
    private val results = mutableListOf<Result>()

    suspend operator fun invoke(): Result {
        headers = store.authKey.firstOrNull()?.headers ?: return Result.failure()

        downloadLoanAccounts()

        return Result.success()
    }

    private suspend fun downloadLoanAccounts() {
        response(
            request = { service.loans(headers) }
        ) { feedback ->
            val loans = feedback.pageItems.toTypedArray()

            loans.forEach {
                val loanId = it.id
                loansDao.insert(it)
                downloadLoanDetailedAccounts(loanId)
            }
        }
    }

    private suspend fun downloadLoanDetailedAccounts(loanId: Int) {
        response(
            { service.detailedLoan(headers, loanId) }
        ) {
            val loan = OfflineDetailedLoan(loanId, it.clientId, loanAccount = it)
            loansDao.insert(loan)
        }
    }
}