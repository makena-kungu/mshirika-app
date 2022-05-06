package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Repayment
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.RepaymentType
import co.ke.mshirika.mshirika_app.data_layer.repositories.LoansRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoansViewModel
@Inject constructor(
    private val repo: LoansRepo
) : MshirikaViewModel() {
    private val _repaymentTypes get() = repo.repaymentTypes
    private val _rTypes = MutableStateFlow(emptyList<RepaymentType>())

    private val _loanAccount = MutableStateFlow<LoanAccount?>(null)

    val loanAccount get() = _loanAccount.asStateFlow()
    val loans: Flow<PagingData<LoanAccount>>
        get() = repo.loans.cachedIn(viewModelScope)

    private val repaymentTypes: StateFlow<List<RepaymentType>>
        get() {
            viewModelScope.launch(IO) {
                repo.repaymentTypes()
                _repaymentTypes.collectLatest { outcome ->
                    outcome.stateHandler(false) {
                        _rTypes.value = it
                    }
                }
            }
            return _rTypes.asStateFlow()
        }

    fun repay(
        amount: String,
        type: String,
        code: String,
        repaymentDate: Long,
        bankDate: Long
    ) {
        val loanAccount = loanAccount.value ?: return
        val id = repaymentTypes.value.find { it.name == type } ?: return
        val repayment = Repayment(
            transactionDate = repaymentDate.mshirikaDate,
            transactionAmount = amount,
            paymentTypeId = id.toString(),
            bankDate = bankDate.mshirikaDate,
            receiptNumber = code
        )

        viewModelScope.launch(IO) {
            val outcome = repo.repay(loanAccount.id, repayment)
            outcome.stateHandler {
                successChannel.send(resourceText(R.string.repayment_successful))
            }
        }
    }

    fun setLoanAccount(loanAccount: LoanAccount) {
        _loanAccount.value = loanAccount
    }
}