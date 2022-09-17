package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more.use_cases.AddBeneficiaryUseCase
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more.use_cases.AddNokUseCase
import co.ke.mshirika.mshirika_app.utility.ld
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MoreViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repo: ClientsRepo,
    private val nokUseCase: AddNokUseCase,
    private val beneficiaryUseCase: AddBeneficiaryUseCase
) : MshirikaViewModel() {

    private val clientId = MutableSharedFlow<Int>()
    private var _template: ClientTemplate?
        get() {
            return state[KEY_TEMPLATE]
        }
        set(value) {
            state[KEY_TEMPLATE] = value
        }

    val familia: ld<List<Family.Fam>> = clientId.mapLatest {
        repo.family(it) ?: emptyList()
    }.asLiveData()
    val beneficiario: ld<Beneficiary?> = clientId.mapLatest {
        repo.beneficiaries(it)
    }.asLiveData()
    val parienteMasCercano: ld<NextOfKin?> = clientId.mapLatest {
        repo.nok(it)
    }.asLiveData()

    suspend fun addNok(
        name: String,
        relationship: String,
        idNumber: String,
        phoneNumber: String
    ): CreateNok? {
        val clientId = clientId.last()
        val template = template() ?: return null
        return nokUseCase(
            clientId,
            name = name,
            idNumber = idNumber,
            relationship = relationship,
            phoneNumber = phoneNumber,
            template = template.familyMemberOptions
        )
    }

    suspend fun addBeneficiary(
        name: String,
        relationship: String,
        phoneNumber: String,
        idNumber: String,
        dob: Long,
        percent: String
    ): CreateBeneficiary? {
        val clientId: Int = clientId.last()
        val template = template() ?: return null
        val relationshipTemplate = template.familyMemberOptions
        return beneficiaryUseCase(
            clientId = clientId,
            name = name,
            relationship = relationship,
            phoneNumber = phoneNumber,
            idNumber = idNumber,
            dob = dob,
            percent = percent,
            template = relationshipTemplate
        )
    }

    fun setClientId(clientId: Int) {
        this.clientId.tryEmit(clientId)
    }

    suspend fun template(): ClientTemplate? {
        if (_template != null) return _template

        _template = repo.template()
        return _template
    }

    companion object {
        private const val KEY_TEMPLATE = "TEMPLATE"
    }
}