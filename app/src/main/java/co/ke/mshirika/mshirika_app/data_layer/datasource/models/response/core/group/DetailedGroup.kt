package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group


import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente

@Keep
data class DetailedGroup(
    val id: Int,
    val accountNo: String,
    val name: String,
    val status: Status,
    val active: Boolean,
    val activationDate: List<Int>,
    val officeId: Int,
    val officeName: String,
    val hierarchy: String,
    val groupLevel: String,
    val clientMembers: List<ClientMember>,
    val activeClientMembers: List<Cliente>,
    val timeline: Timeline
) {
    @Keep
    data class Status(
        val id: Int,
        val code: String,
        val value: String
    )

    @Keep
    data class ClientMember(
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
            val active: Boolean,
            val mandatory: Boolean,
            val id: Int,
            val name: String
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
            val active: Boolean,
            val mandatory: Boolean,
            val id: Int,
            val name: String
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
}