package co.ke.mshirika.mshirika_app.data.request


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui.util.DateUtil
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.mshirikaDate
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CreateCenter(
    val name: String,
    val officeId: Int,
    val submittedOnDate: String,
    val staffId: String = "",
    val externalId: String,
    val active: Boolean = true,
    val activationDate: String = System.currentTimeMillis().mshirikaDate,
    val locale: String = "en",
    val dateFormat: String = DateUtil.DATE_FORMAT,
    val groupMembers: List<String> = emptyList()
) : Parcelable