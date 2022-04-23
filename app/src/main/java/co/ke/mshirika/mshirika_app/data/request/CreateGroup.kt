package co.ke.mshirika.mshirika_app.data.request


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.DATE_FORMAT
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CreateGroup(
    val officeId: String,
    val name: String,
    val externalId: String,
    val dateFormat: String = DATE_FORMAT,
    val locale: String = "en",
    val active: Boolean,
    val activationDate: String? = null,
    val submittedOnDate: String
) : Parcelable