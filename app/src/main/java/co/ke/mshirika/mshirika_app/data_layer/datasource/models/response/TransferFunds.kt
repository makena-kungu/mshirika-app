package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response


import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TransferFunds(
    val commandId: Int,
    val resourceId: Int,
    val rollbackTransaction: Boolean
) : Respondent