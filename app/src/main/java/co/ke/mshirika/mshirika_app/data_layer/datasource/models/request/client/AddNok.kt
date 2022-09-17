package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AddNok(
    @SerializedName("Full Names")
    val fullNames: String,
    @SerializedName("GuarantorRelationship_cd_Relationship")
    val guarantorRelationshipCdRelationship: Int,
    @SerializedName("ID No")
    val iDNo: String,
    @SerializedName("Phone")
    val phone: String,
    val locale: String = "en"
) : Parcelable