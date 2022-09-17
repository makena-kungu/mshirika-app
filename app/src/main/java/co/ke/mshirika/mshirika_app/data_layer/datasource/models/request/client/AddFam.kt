package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class AddFam(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val qualification: String,
    val age: String,
    val isDependent: String,
    val relationshipId: Int,
    val genderId: Int,
    val professionId: String,
    val maritalStatusId: Int,
    val dateOfBirth: String,
    val locale: String,
    val dateFormat: String
) : Parcelable