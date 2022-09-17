package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.guarantors

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateGuarantor
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanWithGuarantors
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.guarantors.GuarantorsTemplateWithClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Feedback
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.SearchRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.utility.ld
import co.ke.mshirika.mshirika_app.utility.mld
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GuarantorsViewModel @Inject constructor(
    private val loansRepo: LoansRepo,
    private val searchRepo: SearchRepo,
    private val stateHandle: SavedStateHandle
) : MshirikaViewModel() {

    private val _bottomSheetState = mld(DEFAULT)
    private val _guarantors = Channel<List<LoanWithGuarantors.Guarantor>>()
    private val _clients = Channel<List<Cliente>>()

    val bottomSheetState: ld<BottomSheetState> get() = _bottomSheetState
    val client: Cliente? get() = stateHandle[CLIENT]
    val clients get() = _clients.receiveAsFlow().asLiveData()
    val data = stateHandle.get<Feedback<Cliente>>(DATA)?.pageItems ?: emptyList()
    val guarantors get() = _guarantors.receiveAsFlow().asLiveData()
    val template: GuarantorsTemplateWithClient get() = stateHandle[TEMPLATE]!!

    private fun update(state: BottomSheetState) {
        _bottomSheetState.value = state
    }

    fun guarantors(guarantors: List<LoanWithGuarantors.Guarantor>) {
        viewModelScope.launch {
            _guarantors.send(guarantors)
        }
    }

    fun expand() {
        update(BottomSheetState.Expanded)
    }

    fun hide() {
        update(BottomSheetState.Hidden)
    }

    fun searchClient(query: String) {
        viewModelScope.launch {
            val outcome = searchRepo.searchClients(query)
            if (outcome is Outcome.Success) {
                val data: Feedback<Cliente> = outcome.data ?: return@launch
                stateHandle[DATA] = data
                _clients.send(data.pageItems)
            }
        }
    }

    fun addGuarantor(name: String, amount: Int, guarantorTypeId: Int, clientId: Int, loanId: Int) {
        if (data.isEmpty()) return
        val g: LoanWithGuarantors.Guarantor? = null
        g?.guarantorType?.id
        val guarantor = data.first { it.displayName == name }


        viewModelScope.launch(IO) {
            val createGuarantor = CreateGuarantor(
                savingsId = guarantor.savingsAccountId,
                amount = amount.toString(),
                guarantorTypeId = guarantorTypeId,
                clientId = clientId
            )
            val outcome = loansRepo.createGuarantor(loanId, createGuarantor)
            if (outcome is Outcome.Success) {
                //refresh template
                val template = loansRepo.guarantorsTemplate(loanId) ?: return@launch
                guarantors(template.guarantors)
            }
        }
    }

    fun selectedClient(client: Cliente, loanId: Int) {
        stateHandle[CLIENT] = client
        viewModelScope.launch {
            val template: GuarantorsTemplateWithClient = loansRepo.guarantorsTemplate(
                clientId = client.id,
                loanId = loanId
            ) ?: return@launch
            stateHandle[TEMPLATE] = template
        }
    }

    sealed class BottomSheetState {
        object Expanded : BottomSheetState()
        object Hidden : BottomSheetState()
        object Collapsed : BottomSheetState()
    }

    companion object {
        val DEFAULT: BottomSheetState = BottomSheetState.Hidden
        const val DATA = "data"
        const val CLIENT = "client"
        const val TEMPLATE = "template"
    }
}