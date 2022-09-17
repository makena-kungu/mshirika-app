package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import androidx.annotation.Keep

@Keep
data class DetailedClient(
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
    val clientNonPersonDetails: ClientNonPersonDetails
) {
    @Keep
    data class Status(
        val id: Int,
        val code: String,
        val value: String
    )

    @Keep
    data class SubStatus(
        val id: Int,
        val name: String,
        val description: String,
        val active: Boolean,
        val mandatory: Boolean
    )

    @Keep
    data class Gender(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    )

    @Keep
    data class ClientType(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    )

    @Keep
    data class ClientClassification(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    )

    @Keep
    data class Timeline(
        val submittedOnDate: List<Int>,
        val submittedByUsername: String,
        val submittedByFirstname: String,
        val submittedByLastname: String,
        val activatedOnDate: List<Int>,
        val activatedByUsername: String,
        val activatedByFirstname: String,
        val activatedByLastname: String
    )

    @Keep
    data class LegalForm(
        val id: Int,
        val code: String,
        val value: String
    )

    @Keep
    data class ClientNonPersonDetails(
        val constitution: Constitution,
        val mainBusinessLine: MainBusinessLine
    ) {
        @Keep
        data class Constitution(
            val active: Boolean,
            val mandatory: Boolean
        )

        @Keep
        data class MainBusinessLine(
            val active: Boolean,
            val mandatory: Boolean
        )
    }
}