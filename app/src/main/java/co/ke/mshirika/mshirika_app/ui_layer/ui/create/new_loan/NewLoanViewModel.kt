package co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.repositories.LoansRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.PageIndicator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewLoanViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repo: LoansRepo
) : MshirikaViewModel() {
    private var _pages = 0

    private val _enableNext = Channel<Boolean>()
    private val _charges = Channel<List<NewLoan.Charge>>()
    private val _pageChannel = Channel<Int>()
    private val _subtitle = Channel<Int>()
    private val template1: NewLoanTemplate get() = state[TEMPLATE1]!!

    var client: Client?
        get() = state[CLIENT]
        set(value) {
            state[CLIENT] = value
        }

    val chargesFlow get() = _charges.receiveAsFlow()
    val enableNext get() = _enableNext.receiveAsFlow()
    val page get() = _pageChannel.receiveAsFlow()
    val subtitle get() = _subtitle.receiveAsFlow()
    val template2: NewLoanTemplate2 get() = state[TEMPLATE2]!!

    val charges: List<NewLoan.Charge> get() = state[CHARGES] ?: emptyList()
    val details: MutableMap<String, String?> get() = state[DETAILS] ?: mutableMapOf()
    val terms: MutableMap<String, String?> get() = state[TERMS] ?: mutableMapOf()

    val indicators = page.map { position ->
        buildList<PageIndicator> {
            val range = 0.._pages
            range.forEach {
                PageIndicator(it, it == position)
            }
        }
    }

    fun cacheDetails(fields: MutableMap<String, String?>) {
        state[DETAILS] = fields
    }

    fun cacheTerms(fields: MutableMap<String, String?>) {
        state[TERMS] = fields
    }

    fun cacheCharges(vararg charges: NewLoan.Charge) {
        val list: MutableList<NewLoan.Charge> = state[CHARGES] ?: mutableListOf()
        list += charges
        state[CHARGES] = list
        viewModelScope.launch {
            _charges.send(list)
        }
    }

    suspend fun getLoanTemplate(client: Client): NewLoanTemplate? {
        var template: NewLoanTemplate? = state[TEMPLATE1]
        if (template == null) {
            state[TEMPLATE1] = repo.getLoanTemplate(clientId = client.id)
            template = state[TEMPLATE1]
        }
        return template
    }

    suspend fun getLoanTemplate(productName: String): NewLoanTemplate2? {
        val template2: NewLoanTemplate2? = state[TEMPLATE2]
        if (template2 != null) {
            return template2
        }
        val productId = template1.productOptions.find { it.name == productName }?.id ?: return null
        val clientId = template1.clientId
        state[TEMPLATE2] = repo.getLoanTemplate(clientId, productId)
        return state[TEMPLATE2]
    }

    fun disableNext() {
        enableNext(false)
    }

    fun enableNext(value: Boolean = true) {
        viewModelScope.launch {
            _enableNext.send(value)
        }
    }

    fun update(position: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            _pageChannel.send(position)
        }
    }

    fun updateSubtitle(@StringRes subtitleId: Int) {
        viewModelScope.launch {
            _subtitle.send(subtitleId)
        }
    }

    fun setPages(pages: Int) {
        _pages = pages
    }

    companion object {
        const val CHARGES = "charges"
        const val DETAILS = "details"
        const val TERMS = "terms"
        const val CLIENT = "client"
        const val TEMPLATE1 = "template1"
        const val TEMPLATE2 = "template2"
    }
}