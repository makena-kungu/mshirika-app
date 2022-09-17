package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent

@Keep
@Parcelize
data class CreateBeneficiary(
    val officeId: Int,
    val clientId: Int,
    val resourceId: Int
) : Respondent