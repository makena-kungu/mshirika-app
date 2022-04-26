package co.ke.mshirika.mshirika_app.data_layer.remote.models.response


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RepaymentSuccessful(
    val officeId: Int,
    val clientId: Int,
    val loanId: Int,
    val resourceId: Int,
    val changes: Changes
) : Respondent {

    @Keep
    @Parcelize
    data class Changes(
        val transactionDate: String,
        val transactionAmount: String,
        val locale: String,
        val dateFormat: String,
        val note: String,
        val accountNumber: String,
        val checkNumber: String,
        val routingCode: String,
        val receiptNumber: String,
        val bankNumber: String
    ) : Parcelable
}