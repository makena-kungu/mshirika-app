package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan

import androidx.lifecycle.asLiveData
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LoanViewModel @Inject constructor(
    private val repo: LoansRepo
) : MshirikaViewModel() {

    private val _loanId = MutableSharedFlow<Int>()
    var loanId: Int
        get() = 0
        set(value) {
            _loanId.tryEmit(value)
        }
    private val _prestamos = _loanId.flatMapLatest { loanId ->
        flowOf(repo.detailedLoanAccount(loanId))
    }

    val prestamo = _prestamos.asLiveData()
}