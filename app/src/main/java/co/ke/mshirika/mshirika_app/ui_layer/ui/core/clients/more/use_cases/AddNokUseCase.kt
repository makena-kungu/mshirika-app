package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more.use_cases

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddNok
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.CreateNok
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import javax.inject.Inject

class AddNokUseCase @Inject constructor(
    private val repo: ClientsRepo
) {

    suspend operator fun invoke(
        clientId: Int,
        name: String,
        relationship: String,
        idNumber: String,
        phoneNumber: String,
        template: ClientTemplate.FamilyMemberOptions
    ): CreateNok? {
        val relationshipId = template
            .relationshipIdOptions
            .firstOrNull { it.name == relationship }?.id ?: return null
        return repo.addNok(
            clientId = clientId,
            addNok = AddNok(
                fullNames = name,
                guarantorRelationshipCdRelationship = relationshipId,
                iDNo = idNumber,
                phone = phoneNumber
            )
        )
    }
}