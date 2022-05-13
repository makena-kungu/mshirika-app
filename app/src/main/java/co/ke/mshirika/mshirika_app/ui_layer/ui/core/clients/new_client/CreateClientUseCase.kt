package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.response.ClientCreationResponse
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.FamilyMember
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.GeneralData
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.age
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import javax.inject.Inject

class CreateClientUseCase @Inject constructor(
    private val repo: ClientsRepo
) {

    suspend operator fun invoke(
        general: GeneralData,
        template: ClientTemplate,
        familyMembers: List<FamilyMember>
    ): ClientCreationResponse? {
        val savingsProductId = template
            .savingProductOptions
            .first { it.name == general.savingsAccount }.id

        return template.run {
            general.run {
                val classification = clientClassificationOptions
                    .first { it.name == clientClassification }.id
                val type = clientTypeOptions.first { it.name == clientType }.id
                val genderId = genderOptions
                    .first { it.name == if (gender) "Male" else "Female" }.id
                // TODO: Check if this option should be allowed
                //val legalsFormId = clientLegalFormOptions.first().id
                val famMembers = familyMembers.map {
                    val names = it.name.split(" ".toRegex())
                    val size = names.size
                    val secondLast = names.lastIndex
                    val firstname = when {
                        size >= 3 -> names.subList(0, secondLast).joinToString(" ")
                        else -> names.first()
                    }
                    val lastName = names.last()
                    CreateClient.FamilyMember(
                        firstName = firstname,
                        lastName = lastName,
                        age = it.dob.age.toString(),
                        relationshipId = it.relationship,
                        genderId = it.gender,
                        maritalStatusId = it.maritalStatus,
                        dateOfBirth = it.dob.mshirikaDate
                    )
                }

                val client = CreateClient(
                    officeId = officeId,
                    firstname = firstName,
                    middlename = middleName,
                    lastname = lastName,
                    externalId = memNo,
                    activationDate = activationDate.mshirikaDate,
                    submittedOnDate = submitDate.mshirikaDate,
                    savingsProductId = savingsProductId,
                    genderId = genderId,
                    //legalFormId = legalsFormId,
                    isStaff = isStaff,
                    familyMembers = famMembers,
                    clientClassificationId = classification,
                    clientTypeId = type,
                    dateOfBirth = dob.mshirikaDate
                )
                repo.createClient(client)
            }
        }
    }
}