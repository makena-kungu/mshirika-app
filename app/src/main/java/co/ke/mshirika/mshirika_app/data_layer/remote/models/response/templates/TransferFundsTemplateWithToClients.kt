package co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TransferFundsTemplateWithToClients(
    val currency: Currency,
    val transferAmount: Int,
    val transferDate: List<Int>,
    val fromOffice: FromOffice,
    val fromClient: FromClient,
    val fromAccountType: FromAccountType,
    val fromAccount: FromAccount,
    val toOffice: ToOffice,
    val fromOfficeOptions: List<FromOfficeOption>,
    val fromClientOptions: List<FromClientOption>,
    val fromAccountTypeOptions: List<FromAccountTypeOption>,
    val fromAccountOptions: List<FromAccountOption>,
    val toOfficeOptions: List<ToOfficeOption>,
    val toClientOptions: List<ToClientOption>,
    val toAccountTypeOptions: List<ToAccountTypeOption>
) : Respondent {
    @Keep
    @Parcelize
    data class Currency(
        val code: String,
        val name: String,
        val decimalPlaces: Int,
        val inMultiplesOf: Int,
        val displaySymbol: String,
        val nameCode: String,
        val displayLabel: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class FromOffice(
        val id: Int,
        val name: String,
        val nameDecorated: String,
        val externalId: String,
        val openingDate: List<Int>,
        val hierarchy: String,
        val parentId: Int,
        val parentName: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class FromClient(
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
        val dateOfBirth: List<Int>,
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
        //val groups: List<Any>,
        val clientNonPersonDetails: ClientNonPersonDetails
    ) : Respondent {
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

    @Keep
    @Parcelize
    data class FromAccountType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class FromAccount(
        val id: Int,
        val accountNo: String,
        val clientId: Int,
        val clientName: String,
        val productId: Int,
        val productName: String,
        val fieldOfficerId: Int,
        val fieldOfficerName: String,
        val currency: Currency
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Currency(
            val code: String,
            val name: String,
            val decimalPlaces: Int,
            val inMultiplesOf: Int,
            val displaySymbol: String,
            val nameCode: String,
            val displayLabel: String
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class ToOffice(
        val id: Int,
        val name: String,
        val nameDecorated: String,
        val externalId: String,
        val openingDate: List<Int>,
        val hierarchy: String,
        val parentId: Int,
        val parentName: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class FromOfficeOption(
        val id: Int,
        val name: String,
        val nameDecorated: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class FromClientOption(
        val id: Int,
        val displayName: String,
        val isStaff: Boolean,
        val officeId: Int,
        val officeName: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class FromAccountTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class FromAccountOption(
        val id: Int,
        val accountNo: String,
        val clientId: Int,
        val clientName: String,
        val productId: Int,
        val productName: String,
        val fieldOfficerId: Int,
        val fieldOfficerName: String,
        val currency: Currency
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Currency(
            val code: String,
            val name: String,
            val decimalPlaces: Int,
            val inMultiplesOf: Int,
            val displaySymbol: String,
            val nameCode: String,
            val displayLabel: String
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class ToOfficeOption(
        val id: Int,
        val name: String,
        val nameDecorated: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ToClientOption(
        val id: Int,
        val displayName: String,
        val isStaff: Boolean,
        val officeId: Int,
        val officeName: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ToAccountTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable
}