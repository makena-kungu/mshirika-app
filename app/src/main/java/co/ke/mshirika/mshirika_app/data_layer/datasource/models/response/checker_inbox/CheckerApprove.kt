package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CheckerApprove(
    val commandId: Int,
    val officeId: Int,
    val clientId: Int,
    val resourceId: Int
) : Parcelable