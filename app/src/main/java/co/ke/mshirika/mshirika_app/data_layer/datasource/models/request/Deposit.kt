package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Deposit(
    val locale: String = "en",
    val dateFormat: String = DateUtil.DATE_FORMAT,
    val transactionDate: String,
    val transactionAmount: String,
    val paymentTypeId: String,
    val accountNumber: String? = null,
    val checkNumber: String? = null,
    val routingCode: String? = null,
    val receiptNumber: String,
    @SerializedName("bankNumber")
    val bankDate: String
) : Parcelable