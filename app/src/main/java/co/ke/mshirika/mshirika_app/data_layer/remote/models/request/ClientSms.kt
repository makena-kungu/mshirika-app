package co.ke.mshirika.mshirika_app.data_layer.remote.models.request


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class ClientSms(
    val clientId: String,
    val message: String
) : Parcelable