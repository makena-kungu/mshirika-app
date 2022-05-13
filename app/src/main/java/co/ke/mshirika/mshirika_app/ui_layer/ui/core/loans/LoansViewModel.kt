package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Repayment
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.RepaymentSuccessful
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.RepaymentType
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.GetsRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
            .asLiveData()
            .cachedIn(this)

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
        val id = _types.firstOrNull{ it.name == type }?.id ?: return null
        Log.d(TAG, "repay: amount = $amount")
        val repayment = Repayment(
            transactionDate = repaymentDate.mshirikaDate,
            transactionAmount = amount,
            paymentTypeId = "$id",
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