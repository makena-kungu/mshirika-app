package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response


import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ClientSmsResponse(
    val resourceId: Int
) : Respondent