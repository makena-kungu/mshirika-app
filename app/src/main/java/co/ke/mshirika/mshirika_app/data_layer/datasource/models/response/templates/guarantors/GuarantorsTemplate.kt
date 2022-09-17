package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.guarantors


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class GuarantorsTemplate(
    val guarantorType: GuarantorType,
    val status: Boolean,
    val guarantorTypeOptions: List<GuarantorTypeOption>,
    val allowedClientRelationshipTypes: List<AllowedClientRelationshipType>
) : Parcelable {
    @Keep
    @Parcelize
    data class GuarantorType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class GuarantorTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class AllowedClientRelationshipType(
        val id: Int,
        val name: String,
        val position: Int,
        val active: Boolean,
        val mandatory: Boolean,
        val description: String
    ) : Parcelable
}