package co.ke.mshirika.mshirika_app.data.request


import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.DATE_FORMAT
import java.util.*

@Keep
data class Repayment(
    val dateFormat: String = DATE_FORMAT,
    val locale: String = Locale.getDefault().toLanguageTag(),
    val transactionDate: String,
    val transactionAmount: String,
    val paymentTypeId: String,
    val note: String = "",
    val accountNumber: String = "",
    val checkNumber: String = "",
    val routingCode: String = "",
    val receiptNumber: String,
    val bankNumber: String // this will be used as the bank date
)