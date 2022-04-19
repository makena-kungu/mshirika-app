package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CreateClientTemplate(
    val activationDate: List<Int>,
    val isStaff: Boolean,
    val officeId: Int,
    val officeOptions: List<OfficeOption>,
    val staffOptions: List<StaffOption>,
    val savingProductOptions: List<SavingProductOption>,
    val genderOptions: List<GenderOption>,
    val clientTypeOptions: List<ClientTypeOption>,
    val clientClassificationOptions: List<ClientClassificationOption>,
    val clientNonPersonConstitutionOptions: List<ClientNonPersonConstitutionOption>,
    val clientLegalFormOptions: List<ClientLegalFormOption>,
    val familyMemberOptions: FamilyMemberOptions,
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