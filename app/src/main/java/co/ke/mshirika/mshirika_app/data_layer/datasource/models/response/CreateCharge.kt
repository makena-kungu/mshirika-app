package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CreateCharge(
    val chargeId: Int,
    val amount: Int,
    val dueDate: String = System.currentTimeMillis().mshirikaDate,
    val locale: String = "en",
    val dateFormat: String = DateUtil.DATE_FORMAT
) : Parcelable