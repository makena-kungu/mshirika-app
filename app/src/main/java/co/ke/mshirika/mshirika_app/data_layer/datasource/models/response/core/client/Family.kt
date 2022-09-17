package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mediumDate
import kotlinx.parcelize.Parcelize

@Parcelize
class Family : ArrayList<Family.Fam>(), Respondent {
    @Keep
    @Parcelize
    data class Fam(
        val id: Int,
        val clientId: Int,
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val qualification: String,
        val relationshipId: Int,
        val relationship: String,
        val maritalStatusId: Int,
        val maritalStatus: String,
        val genderId: Int,
        val gender: String,
        val dateOfBirth: List<Int>,
        val professionId: Int,
        val mobileNumber: String,
        val age: Int,
        val isDependent: Boolean
    ) : Parcelable {
        val dob get() = dateOfBirth.mediumDate
        val name get() = "$firstName $middleName $lastName"

        companion object : DiffUtil.ItemCallback<Fam>() {
            override fun areItemsTheSame(oldItem: Fam, newItem: Fam): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Fam, newItem: Fam): Boolean {
                return oldItem == newItem
            }
        }
    }
}