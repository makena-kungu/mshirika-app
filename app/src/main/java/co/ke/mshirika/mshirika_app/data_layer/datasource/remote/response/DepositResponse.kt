package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DepositResponse(
    val officeId: Int,
    val clientId: Int,
    val savingsId: Int,
    val resourceId: Int,
    val changes: Changes
) : Respondent {
    @Keep
    @Parcelize
    data class Changes(
        val receiptNumber: String,
        val bankNumber: String
    ) : Parcelable
}