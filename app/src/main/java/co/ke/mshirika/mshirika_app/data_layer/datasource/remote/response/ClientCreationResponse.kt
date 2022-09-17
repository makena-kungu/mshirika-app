package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response


import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ClientCreationResponse(
    val officeId: Int,
    val clientId: Int,
    val resourceId: Int,
    val savingsId: Int
) : Respondent