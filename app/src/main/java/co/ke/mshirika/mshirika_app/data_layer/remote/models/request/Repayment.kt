package co.ke.mshirika.mshirika_app.data_layer.remote.models.request


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.DATE_FORMAT
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Keep
@Parcelize
data class Repayment(
    val dateFormat: String = DATE_FORMAT,
    val locale: String = "en",
    val transactionDate: String,
    val transactionAmount: String,
    val paymentTypeId: String,
    val note: String = "",
    val accountNumber: String = "",
    val checkNumber: String = "",
    val routingCode: String = "",
    val receiptNumber: String,
    @SerializedName("bankNumber")
    val bankDate: String // this will be used as the bank date
):Parcelable