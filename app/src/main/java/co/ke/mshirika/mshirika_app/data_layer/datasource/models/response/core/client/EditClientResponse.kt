package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class EditClientResponse(
    val officeId: Int,
    val clientId: Int,
    val resourceId: Int
) : Parcelable