package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.CreateLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.CreateLoanUseCase
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.NewLoanDetailsFragment.Companion.Details
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.NewLoanTermsFragment.Companion.Terms
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.PageIndicator
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import co.ke.mshirika.mshirika_app.utility.ld
import co.ke.mshirika.mshirika_app.utility.mld
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewLoanViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repo: LoansRepo,
    private val createLoanUseCase: CreateLoanUseCase
) : MshirikaViewModel() {
    private var _pages = 0

    private val _enableNext = Channel<Boolean>()
    private val _charges = Channel<List<NewLoan.Charge>>()
    private val _pageChannel = Channel<Int>()
    private val _subtitle = Channel<Int>()
    private val _created = mld<CreateLoan>()
    private val template1: NewLoanTemplate get() = state[TEMPLATE1]!!

    var client: Client?
        get() = state[CLIENT]
        set(value) {
            state[CLIENT] = value
        }

    var terms :Terms?
    get() = state[TERMS]
    set(value) {
        state[TERMS] = value
    }

    val chargesFlow get() = _charges.receiveAsFlow()
    val enableNext get() = _enableNext.receiveAsFlow()
    val page get() = _pageChannel.receiveAsFlow()
    val subtitle get() = _subtitle.receiveAsFlow()
    val created: ld<CreateLoan> = _created
    val template2: NewLoanTemplate2 get() = state[TEMPLATE2]!!

    val charges: List<NewLoan.Charge> get() = state[CHARGES] ?: emptyList()
    val details: Details? get() = state[DETAILS]

    val indicators = page.map { position ->
        buildList<PageIndicator> {
            val range = 0.._pages
            range.forEach {
                PageIndicator(it, it == position)
            }
        }
    }.asLiveData()

    fun cacheDetails(details: Details) {
        state[DETAILS] = details
    }

    fun cacheTerms(terms: Terms) {
        this.terms = terms
    }

    fun cacheCharges(vararg charges: NewLoan.Charge) {
        val list: MutableList<NewLoan.Charge> = state[CHARGES] ?: mutableListOf()
        list += charges
        state[CHARGES] = list
        viewModelScope.launch {
            _charges.send(list)
        }
    }

    fun createNewLoan(savingsAccountId:Int) {
        viewModelScope.launch(IO) {
            val details = details ?: return@launch
            val terms = terms ?: return@launch

            val response = createLoanUseCase(savingsAccountId, details, terms, template2, charges)
            response.stateHandler {
                _created.postValue(it)
                successChannel.send(resourceText(R.string.loan_successful))
            }
        }
    }

    suspend fun getLoanTemplate(client: Client): NewLoanTemplate? = withContext(IO) {
        var template: NewLoanTemplate? = state[TEMPLATE1]
        if (template == null) {
            state[TEMPLATE1] = repo.getLoanTemplate(clientId = client.id)
            template = state[TEMPLATE1]
        }
        template
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