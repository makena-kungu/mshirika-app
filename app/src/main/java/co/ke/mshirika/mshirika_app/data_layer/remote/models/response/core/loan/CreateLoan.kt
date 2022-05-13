package co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent

@Keep
@Parcelize
data class CreateLoan(
    val officeId: Int,
    val clientId: Int,
    val loanId: Int,
    val resourceId: Int
) : Respondent