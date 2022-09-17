package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "client_template")
data class ClientTemplate(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @TypeConverters(ClientTemplateConverter::class)
    val clientTypeOptions: List<ClientTypeOption>,
    @TypeConverters(ClientTemplateConverter::class)
    val clientClassificationOptions: List<ClientClassificationOption>,
    @TypeConverters(ClientTemplateConverter::class)
    val savingProductOptions: List<SavingProductOption>,
    @TypeConverters(ClientTemplateConverter::class)
    val familyMemberOptions: FamilyMemberOptions,
    @TypeConverters(co.ke.mshirika.mshirika_app.data_layer.datasource.local.Converter::class)
    val activationDate: List<Int>,
    val isStaff: Boolean,
    val officeId: Int,
    @TypeConverters(ClientTemplateConverter::class)
    val officeOptions: List<OfficeOption>,
    @TypeConverters(ClientTemplateConverter::class)
    val staffOptions: List<StaffOption>,
    @TypeConverters(ClientTemplateConverter::class)
    val genderOptions: List<GenderOption>,
    @TypeConverters(ClientTemplateConverter::class)
    val clientNonPersonConstitutionOptions: List<ClientNonPersonConstitutionOption>,
    @TypeConverters(ClientTemplateConverter::class)
    val clientLegalFormOptions: List<ClientLegalFormOption>,
    val isAddressEnabled: Boolean
) : Respondent {
    @Keep
    @Parcelize
    data class OfficeOption(
        val id: Int,
        val name: String,
        val nameDecorated: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class StaffOption(
        val id: Int,
        val firstname: String,
        val lastname: String,
        val displayName: String,
        val officeId: Int,
        val officeName: String,
        val isLoanOfficer: Boolean,
        val isActive: Boolean,
        val mobileNo: String,
        val joiningDate: List<Int>
    ) : Parcelable

    @Keep
    @Parcelize
    data class SavingProductOption(
        val id: Int,
        val name: String,
        val withdrawalFeeForTransfers: Boolean,
        val allowOverdraft: Boolean,
        val enforceMinRequiredBalance: Boolean,
        val withHoldTax: Boolean,
        val releaseguarantor: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class GenderOption(
        val id: Int,
        val name: String,
        val position: Int,
        val description: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class ClientTypeOption(
        val id: Int,
        val name: String,
        val position: Int,
        val description: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class ClientClassificationOption(
        val id: Int,
        val name: String,
        val position: Int,
        val description: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class ClientNonPersonConstitutionOption(
        val id: Int,
        val name: String,
        val position: Int,
        val description: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class ClientLegalFormOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class FamilyMemberOptions(
        val relationshipIdOptions: List<RelationshipIdOption>,
        val genderIdOptions: List<GenderIdOption>,
        val maritalStatusIdOptions: List<MaritalStatusIdOption>
    ) : Parcelable {
        @Keep
        @Parcelize
        data class RelationshipIdOption(
            val id: Int,
            val name: String,
            val position: Int,
            val active: Boolean,
            val mandatory: Boolean
        ) : Parcelable

        @Keep
        @Parcelize
        data class GenderIdOption(
            val id: Int,
            val name: String,
            val position: Int,
            val description: String,
            val active: Boolean,
            val mandatory: Boolean
        ) : Parcelable

        @Keep
        @Parcelize
        data class MaritalStatusIdOption(
            val id: Int,
            val name: String,
            val position: Int,
            val description: String,
            val active: Boolean,
            val mandatory: Boolean
        ) : Parcelable
    }
}