package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreateGuarantor(
    val clientRelationshipTypeId: String = "",
    val savingsId: Int,
    val amount: String,
    val locale: String = "en",
    val guarantorTypeId: Int,
    @SerializedName("entityId")
    val clientId: Int
)