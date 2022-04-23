package co.ke.mshirika.mshirika_app.remote.response


import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CenterCreatedResponse(
    val officeId: Int,
    val groupId: Int,
    val resourceId: Int
) : Respondent