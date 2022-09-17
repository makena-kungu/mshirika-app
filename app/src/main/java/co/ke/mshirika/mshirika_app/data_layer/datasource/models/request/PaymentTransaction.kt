package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaymentTransaction(
    val transactionDate: String,
    val transactionAmount: String,
    val paymentTypeId: Int,
    val note: String = "",
    val accountNumber: String = "",
    val checkNumber: String = "",
    val routingCode: String = "",
    val receiptNumber: String,
    @SerializedName("bankNumber")
    val bankDate: String,
    val locale: String = "en",
    val dateFormat: String = DateUtil.DATE_FORMAT
) : Parcelable