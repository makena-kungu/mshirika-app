package co.ke.mshirika.mshirika_app.data.request


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.DATE_FORMAT
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Client(
    val officeId: Int,
    val firstname: String,
    val lastname: String,
    val externalId: String,
    val dateFormat: String,
    val locale: String,
    val active: Boolean,
    val activationDate: String,
    val submittedOnDate: String,
    val savingsProductId: Int,
    val datatables: List<Datatable>
) : Parcelable {
    @Keep
    @Parcelize
    data class Datatable(
        val registeredTableName: String,
        val data: Data
    ) : Parcelable {
        @Parcelize
        @Keep
        data class Data(
            val locale: String,
            @SerializedName("Number of members")
            val numberOfMembers: String,
            @SerializedName("Number of dependents")
            val numberOfDependents: String,
            @SerializedName("No of Children")
            val noOfChildren: String,
            @SerializedName("Date of verification")
            val dateOfVerification: String,
            val dateFormat: String = DATE_FORMAT,
            @SerializedName("Address Line")
            val addressLine: String,
            val Street: String,
            val Landmark: String,
            val COUNTRY_cd_Country: Int,
            val STATE_cd_State: String,
            val DISTRICT_cd_District: String,
            @SerializedName("Pincode")
            val pinCode: String
        ) : Parcelable
    }
}