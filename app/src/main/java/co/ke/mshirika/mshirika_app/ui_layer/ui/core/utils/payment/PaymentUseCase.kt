package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.payment

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import co.ke.mshirika.mshirika_app.data_layer.repositories.PaymentRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.floor

class PaymentUseCase @Inject constructor(
    private val repo: PaymentRepo
) {

    suspend operator fun invoke(
        transactionDate: String,
        paymentTypeId: Int,
        bankDate: String,
        receiptNumber: String,
        deposits: List<PaymentDeposit>,
        loanRepayments: List<PaymentLoan>
    ) {
        deposits.forEach { (savingsAccountId, amount, charges) ->
            var depositAmount = amount
            charges.forEach { (chargeId, amount) ->
                depositAmount -= amount
                val charge = PaymentTransaction(
                    transactionAmount = amount.amount,
                    transactionDate = transactionDate,
                    paymentTypeId = paymentTypeId,
                    bankDate = bankDate,
                    receiptNumber = receiptNumber
                )
                withContext(IO) { repo.payCharge(savingsAccountId, chargeId, charge) }
            }

            val deposit = PaymentTransaction(
                transactionAmount = depositAmount.amount,
                transactionDate = transactionDate,
                paymentTypeId = paymentTypeId,
                bankDate = bankDate,
                receiptNumber = receiptNumber
            )
            withContext(IO) { repo.deposit(savingsAccountId, deposit) }
        }
        loanRepayments.forEach { (loanId, amount, charges) ->
            val paymentTransaction = PaymentTransaction(
                transactionAmount = amount.amount,
                transactionDate = transactionDate,
                paymentTypeId = paymentTypeId,
                bankDate = bankDate,
                receiptNumber = receiptNumber
            )
            repo.repayLoan(loanId, paymentTransaction)
        }
    }

    companion object {
        private val Double.amount: String
            get() {
                return floor(this).toInt().toString()
            }
    }
}

data class PaymentCharge(
    val id: Int,
    val amount: Double
)

data class PaymentDeposit(
    val id: Int,
    val amount: Double,
    val charges: List<PaymentCharge> = emptyList()
)

data class PaymentLoan(
    val id: Int,
    val amount: Double,
    val charges: List<PaymentCharge> = emptyList()
)
