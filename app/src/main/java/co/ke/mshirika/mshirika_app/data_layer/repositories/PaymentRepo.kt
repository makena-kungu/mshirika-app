package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.DepositShares
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Repayment
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Loan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.SavingsAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.response.ClientPaymentTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRepo @Inject constructor(
    private val repo: PreferencesStoreRepository,
    private val service: ClientsService,
    private val loansService: LoansService
) {

    suspend fun pay(
        shares: Triple<SavingsAccount, Double, Double>?,
        loans: MutableMap<Loan, Pair<Double, Double>>,
        other: Other
    ) = withContext(IO) {
        val headers = headers()
        shares?.apply {
            val savingsAccount = first
            val amount = second
            //todo find out how ton include a charge
            val charge = if (third > 0) third else null
            respond {
                val deposit = DepositShares(
                    transactionAmount = amount.toInt().toString(),
                    transactionDate = other.transactionDate.mshirikaDate,
                    paymentTypeId = other.modeId,
                    bankDate = other.bankDate.mshirikaDate,
                    receiptNumber = other.receiptNo
                )
                service.deposit(
                    headers,
                    savingsAccount.id,
                    deposit = deposit
                )
            }
        }

        loans.forEach { (loan, pair) ->
            loansService.repay(
                headers = headers,
                loanId = loan.id,
                repayment = Repayment(
                    transactionDate = other.transactionDate.mshirikaDate,
                    transactionAmount = pair.first.toString(),
                    paymentTypeId = other.modeId.toString(),
                    receiptNumber = other.receiptNo,
                    bankDate = other.bankDate.mshirikaDate
                )
            )
        }
    }


    suspend fun queryTemplate(clientId: Int): Outcome<ClientPaymentTemplate> = withContext(IO) {
        respond {
            service.templateClientPayment(headers(), clientId)
        }
    }

    suspend fun headers() = withContext(IO) {
        val key = async {
            repo.authKey
        }
        key.await().first()!!.headers
    }
}

data class Other(
    val transactionDate: Long,
    val modeId: Int,
    val receiptNo: String,
    val bankDate: Long
)