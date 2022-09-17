package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent

@Keep
@Parcelize
data class CreateNok(
    val commandId: Int,
    val resourceId: Int,
    val rollbackTransaction: Boolean
) : Respondent