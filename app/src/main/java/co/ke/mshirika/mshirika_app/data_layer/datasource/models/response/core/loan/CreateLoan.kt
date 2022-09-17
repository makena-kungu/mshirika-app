package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan


import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CreateLoan(
    val officeId: Int,
    val clientId: Int,
    val loanId: Int,
    val resourceId: Int
) : Respondent