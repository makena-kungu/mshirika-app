package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
data class EditClientTemplate(
    val id: Int,
    val accountNo: String,
    val externalId: String,
    val status: Status,
    val subStatus: SubStatus,
    val active: Boolean,
    val activationDate: List<Int>,
    val firstname: String,
    val middlename: String,
    val lastname: String,
    val displayName: String,
    val mobileNo: String,
    val gender: Gender,
    val clientType: ClientType,
    val clientClassification: ClientClassification,
    val isStaff: Boolean,
    val officeId: Int,
    val officeName: String,
    val staffId: Int,
    val staffName: String,
    val timeline: Timeline,
    val savingsProductId: Int,
    val savingsProductName: String,
    val savingsAccountId: Int,
    val legalForm: LegalForm,
    val nationalId: String,
    val groups: List<Any>,
    val officeOptions: List<OfficeOption>,
    val staffOptions: List<StaffOption>,
    val savingProductOptions: List<SavingProductOption>,
    val savingAccountOptions: List<SavingAccountOption>,
    val genderOptions: List<GenderOption>,
    val clientTypeOptions: List<ClientTypeOption>,
    val clientClassificationOptions: List<ClientClassificationOption>,
    val clientNonPersonConstitutionOptions: List<ClientNonPersonConstitutionOption>,
    val clientNonPersonMainBusinessLineOptions: List<Any>,
    val clientLegalFormOptions: List<ClientLegalFormOption>,
    val familyMemberOptions: FamilyMemberOptions,
    val clientNonPersonDetails: ClientNonPersonDetails
) {
    @Keep
    @Parcelize
    data class Status(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class SubStatus(
        val id: Int,
        val name: String,
        val description: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class Gender(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class ClientType(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class ClientClassification(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class Timeline(
        val submittedOnDate: List<Int>,
        val submittedByUsername: String,
        val submittedByFirstname: String,
        val submittedByLastname: String,
        val activatedOnDate: List<Int>,
        val activatedByUsername: String,
        val activatedByFirstname: String,
        val activatedByLastname: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class LegalForm(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

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
        val displayName: String
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
    data class SavingAccountOption(
        val id: Int,
        val accountNo: String,
        val depositType: DepositType,
        val savingsProductId: Int,
        val savingsProductName: String,
        val status: Status,
        val withdrawalFeeForTransfers: Boolean,
        val allowOverdraft: Boolean,
        val enforceMinRequiredBalance: Boolean,
        val withHoldTax: Boolean,
        val isDormancyTrackingActive: Boolean
    ) : Parcelable {
        @Keep
        @Parcelize
        data class DepositType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class Status(
            val id: Int,
            val code: String,
            val value: String,
            val submittedAndPendingApproval: Boolean,
            val approved: Boolean,
            val rejected: Boolean,
            val withdrawnByApplicant: Boolean,
            val active: Boolean,
            val closed: Boolean,
            val prematureClosed: Boolean,
            val transferInProgress: Boolean,
            val transferOnHold: Boolean,
            val matured: Boolean
        ) : Parcelable
    }

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
    data class FamilyMemberOptions(
        val relationshipIdOptions: List<RelationshipIdOption>,
        val genderIdOptions: List<GenderIdOption>,
        val maritalStatusIdOptions: List<MaritalStatusIdOption>,
        val professionIdOptions: List<Any>
    )  {
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

    @Keep
    @Parcelize
    data class ClientNonPersonDetails(
        val constitution: Constitution,
        val mainBusinessLine: MainBusinessLine
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Constitution(
            val active: Boolean,
            val mandatory: Boolean
        ) : Parcelable

        @Keep
        @Parcelize
        data class MainBusinessLine(
            val active: Boolean,
            val mandatory: Boolean
        ) : Parcelable
    }
}