package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.EditClientResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.EditClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.GeneralData
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import javax.inject.Inject

class EditClientUseCase @Inject constructor(
    private val repo: ClientsRepo
) {

    suspend operator fun invoke(
        general: GeneralData,
        template: EditClientTemplate,
    ): EditClientResponse? {
        val savingsProductId = template
            .savingProductOptions
            .firstOrNull { it.name == general.savingsAccount }?.id ?: template.savingsAccountId

        val result = template.run {
            general.run {
                val classification = clientClassificationOptions
                    .first { it.name == clientClassification }.id
                val type = clientTypeOptions.first { it.name == clientType }.id
                val genderId = genderOptions
                    .first {
                        it.name == when {
                            gender -> "Male"
                            else -> "Female"
                        }
                    }.id
                // TODO: Check if this option should be allowed
                //val legalsFormId = clientLegalFormOptions.first().id

                val client = CreateClient(
                    id = 0,
                    officeId = officeId,
                    firstname = firstName,
                    middlename = middleName,
                    lastname = lastName,
                    externalId = memNo,
                    activationDate = activationDate.mshirikaDate,
                    submittedOnDate = submitDate.mshirikaDate,
                    savingsProductId = savingsProductId,
                    genderId = genderId,
                    isStaff = isStaff,
                    familyMembers = emptyList(),
                    clientClassificationId = classification,
                    clientTypeId = type,
                    dateOfBirth = dob.mshirikaDate
                )
                repo.editClient(client)
            }
        }
        return result
    }
}