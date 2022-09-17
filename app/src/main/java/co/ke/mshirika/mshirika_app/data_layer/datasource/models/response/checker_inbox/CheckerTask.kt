package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox

import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.properCase
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Keep
@Parcelize
data class CheckerTask(
    val id: Int,
    val actionName: String,
    val entityName: String,
    val resourceId: Int,
    val maker: String,
    val madeOnDate: Long,
    @SerializedName("processingResult")
    val result: String,
    val officeName: String,
    val clientName: String,
    val loanAccountNo: String,
    val clientId: Int,
    val loanId: Int,
    val url: String,
    val subresourceId: Int,
    val savingsAccountNo: String
) : Parcelable {

    val date: String
        get() {
            val date = Date(madeOnDate)
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return dateFormat.format(date)
        }

    val status: String
        get() {
            val (first, second) = result.split('.')

            return "$first $second".properCase()
        }

    val timeStamp: Timestamp
        get() = Timestamp(madeOnDate)
}