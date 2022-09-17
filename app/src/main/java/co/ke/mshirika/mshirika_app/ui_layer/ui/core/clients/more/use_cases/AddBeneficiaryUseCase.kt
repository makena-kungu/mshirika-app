package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more.use_cases

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddBeneficiary
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddBeneficiary.Companion.date
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.CreateBeneficiary
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import javax.inject.Inject

class AddBeneficiaryUseCase @Inject constructor(
    private val repo: ClientsRepo
) {

    suspend operator fun invoke(
        clientId: Int,
        name: String,
        relationship: String,
        phoneNumber: String,
        idNumber: String,
        dob: Long,
        percent: String,
        template: ClientTemplate.FamilyMemberOptions
    ): CreateBeneficiary? {
        val relationshipId = template.relationshipIdOptions
            .find { it.name == relationship }?.id ?: return null

        val beneficiary = AddBeneficiary(
            name = name,
            relationship = relationshipId,
            phoneNumber = phoneNumber,
            idNumber = idNumber,
            dob = dob.date,
            percent = percent
        )
        return repo.addBeneficiary(clientId, beneficiary)
    }
}