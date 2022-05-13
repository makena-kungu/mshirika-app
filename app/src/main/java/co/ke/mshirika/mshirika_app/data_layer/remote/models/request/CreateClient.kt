package co.ke.mshirika.mshirika_app.data_layer.remote.models.request


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CreateClient(
    val officeId: Int,
    val legalFormId: Int? = null,
    val isStaff: Boolean,
    val active: Boolean = true,
    val genderId: Int,
    val dateOfBirth: String,
    val clientTypeId: Int,
    val clientClassificationId: Int,
    val submittedOnDate: String = mshirikaDate,
    val firstname: String,
    val middlename: String,
    val lastname: String,
    val activationDate: String = mshirikaDate,
    val savingsProductId: Int,
    val familyMembers: List<FamilyMember>,
    val dateFormat: String = DateUtil.DATE_FORMAT,
    val locale: String = "en",
    val externalId: String,
    val address: List<String> = emptyList()
) : Parcelable {

    @Keep
    @Parcelize
    data class FamilyMember(
        val firstName: String,
        val lastName: String,
        val age: String,
        val relationshipId: Int,
        val genderId: Int,
        val maritalStatusId: Int,
        val dateOfBirth: String,
        val dateFormat: String = DateUtil.DATE_FORMAT,
        val locale: String = "en"
    ) : Parcelable
}