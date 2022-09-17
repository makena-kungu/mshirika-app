package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Keep
@Parcelize
data class AddBeneficiary(
    @SerializedName("Full Name")
    val name: String,
    @SerializedName("GuarantorRelationship_cd_Relationship")
    val relationship: Int,
    @SerializedName("Phone Number")
    val phoneNumber: String,
    @SerializedName("ID Number")
    val idNumber: String,
    @SerializedName("Date of Birth")
    val dob: String,
    @SerializedName("Note")
    val note: String = "",
    @SerializedName("Percentage Allocation")
    val percent: String,
    val locale: String = "en",
    val dateFormat: String = FORMAT
) : Parcelable {
    companion object {
        const val FORMAT = "yyyy-MM-dd"
        val Long.date: String
            get() = SimpleDateFormat(FORMAT, Locale.getDefault())
                .format(Date(this))
    }
}