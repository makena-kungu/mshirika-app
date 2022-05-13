package co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan


import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CreateGuarantorResponse(
    val officeId: Int,
    val loanId: Int,
    val resourceId: Int
) : Respondent