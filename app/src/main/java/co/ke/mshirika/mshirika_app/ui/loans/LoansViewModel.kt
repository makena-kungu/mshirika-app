package co.ke.mshirika.mshirika_app.ui.loans

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data.request.Repayment
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.RepaymentType
import co.ke.mshirika.mshirika_app.repositories.LoansRepo
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui.MshirikaViewModel
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

    fun repay(
        amount: String,
        type: String,
        code: String,
        repaymentDate: Long,
        bankDate: Long
    ) {
        repaymentTypes.value.find { it.name == type }?.run {
            val repayment = Repayment(
                transactionDate = repaymentDate.mshirikaDate,
                transactionAmount = amount,
                paymentTypeId = id.toString(),
                bankNumber = bankDate.mshirikaDate,
                receiptNumber = code
            )

            viewModelScope.launch(IO) {
                loanAccount.value?.run {
                    repo.repay(id, repayment)
                }
                repo.repaymentStatus.collectLatest {
                    it.stateHandler {
                        data?.let { successChannel.send("Repayment Successful") }
                    }
                }
            }
        }
    }

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
                        data?.let { _rTypes.value = it }
                    }
                }
            }
            return _rTypes.asStateFlow()
        }

    fun setLoanAccount(loanAccount: LoanAccount) {
        _loanAccount.value = loanAccount
    }
}