package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.PaymentTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.RepaymentType
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.GetsRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LoansViewModel
@Inject constructor(
    private val repo: LoansRepo,
    private val state: SavedStateHandle
) : MshirikaViewModel(), GetsRepaymentSchedule {

    private var _types: List<RepaymentType>
        get() {
            return state[KEY_REPAYMENT_TYPES] ?: emptyList()
        }
        set(value) {
            state[KEY_REPAYMENT_TYPES] = value
        }

    val loans: LiveData<PagingData<ConservativeLoanAccount>>
        get() = repo.loans
            .mapLatest { pagingData -> pagingData.filter { it.status.active } }
            .asLiveData()
            .cachedIn(this)

    val prestamos
        get() = repo.prestamos
            .asLiveData()

    suspend fun detailedLoanAccount(loanId: Int) = withContext(IO) {
        repo.detailedLoanAccount(loanId)
    }

    suspend fun repaymentTypes() = withContext(IO) {
        val types: RepaymentResponse = repo.repaymentTypes() ?: return@withContext null
        repaymentTypes(types)
    }

    private fun repaymentTypes(types: RepaymentResponse): RepaymentResponse {
        _types = types
        return types
    }

    suspend fun repay(
        loanId: Int,
        amount: String,
        type: String,
        code: String,
        repaymentDate: Long,
        bankDate: Long
    ): RepaymentSuccessful? {
        val id = _types.firstOrNull { it.name == type }?.id ?: return null
        Log.d(TAG, "repay: amount = $amount")
        val repayment = PaymentTransaction(
            transactionDate = repaymentDate.mshirikaDate,
            transactionAmount = amount,
            paymentTypeId = id,
            bankDate = bankDate.mshirikaDate,
            receiptNumber = code
        )

        return withContext(IO) { repo.repay(loanId, repayment) }
    }


    companion object {
        private const val TAG = "LoansViewModel"
        private const val KEY_REPAYMENT_TYPES = "REPAYMENT_TYPES"
    }

    override suspend fun repaymentSchedule(loanId: Int): LoanRepaymentSchedule? = withContext(IO) {
        repo.repaymentSchedule(loanId)
    }
}