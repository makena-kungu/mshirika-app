package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.ClientDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.LoansDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings.OfflineCharge
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings.OfflineDeposit
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.loans.OfflineRepayLoan
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.CreateCharge
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.ClientPaymentTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRepo @Inject constructor(
    private val store: PreferencesStoreRepository,
    private val service: ClientsService,
    private val loansService: LoansService,
    private val dao: ClientDao,
    private val loansDao: LoansDao
) {
    suspend fun accounts(clientId: Int) = withContext(IO) {
        respondWithSuccess {
            service.accounts(
                headers = headers(),
                clientId = clientId
            )
        }
    }

    suspend fun account(savingsAccountId: Int) = respondWithSuccess {
        service.account(headers(), savingsAccountId)
    }

    suspend fun charges(savingsAccountId: Int) = respondWithSuccess {
        service.charges(headers(), savingsAccountId)
    }

    suspend fun deposit(
        savingsAccountId: Int,
        deposit: PaymentTransaction
    ) {
        val isSynced = store.isOffline.single()
        if (isSynced) {
            dao.insert(OfflineDeposit(savingsAccountId = savingsAccountId, deposit = deposit))
            return
        }
        service.deposit(
            headers = headers(),
            savingsAccountId = savingsAccountId,
            deposit = deposit
        )
    }

    suspend fun payCharge(
        savingsAccountId: Int,
        chargeId: Int,
        charge: PaymentTransaction
    ) {
        val isSynced = store.isOffline.single()
        if (isSynced) {
            dao.insert(
                OfflineCharge(
                    id = 0,
                    savingsAccountId = savingsAccountId,
                    chargeId = chargeId,
                    charge = charge
                )
            )
            return
        }
        service.charge(
            headers = headers(),
            savingsAccountId = savingsAccountId,
            chargeId = chargeId,
            charge = charge
        )
    }

    suspend fun repayLoan(loanId: Int, loan: PaymentTransaction) {
        val isSynced = store.isOffline.single()
        if (isSynced) {
            loansDao.insert(OfflineRepayLoan(loanId = loanId, loan = loan, id = 0))
            return
        }
        loansService.repay(
            headers = headers(),
            loanId = loanId,
            repayment = loan
        )
    }

    suspend fun queryChargeTemplate1(savingsAccountId: Int) = respondWithSuccess {
        service.chargeTemplate(headers(), savingsAccountId)
    }

    suspend fun queryChargeTemplate2(chargeId: Int) = respondWithSuccess {
        service.chargeTemplate2(headers(), chargeId)
    }

    suspend fun queryTemplate(clientId: Int): ClientPaymentTemplate? = withContext(IO) {
        respondWithSuccess {
            service.templateClientPayment(headers(), clientId)
        }
    }

    suspend fun queryOfflineTemplate(clientId: Int): ClientPaymentTemplate? = withContext(IO) {
        dao.clientPaymentTemplate(clientId)
    }

    suspend fun headers() = withContext(IO) {
        val key = async {
            store.authKey
        }
        key.await().first()!!.headers
    }

    suspend fun createCharge(savingsAccountId: Int, createCharge: CreateCharge) {
        service.charge(headers(), savingsAccountId = savingsAccountId, createCharge)
    }
}

data class Other(
    val transactionDate: Long,
    val modeId: Int,
    val receiptNo: String,
    val bankDate: Long
)