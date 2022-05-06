package co.ke.mshirika.mshirika_app.ui_layer.ui.transactions

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Transaction
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.FlowUtils.collectLatestNonNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor() : MshirikaViewModel() {

    private val transaction = MutableStateFlow<Transaction?>(null)

    val data = flow<List<Pair<Int, String>>> {
        transaction.collectLatestNonNull { (_, _, _, _, _, id, _, paymentDetailData, _, _, submittedByUsername, _, transactionType) ->
            val list = mutableListOf(
                R.string.transaction_id to id.toString(),
                R.string.transaction_type to transactionType.value,
                R.string.transaction_date to paymentDetailData.transactionDate,
                R.string.by to submittedByUsername,
                R.string.payment_type to paymentDetailData.paymentType.name,
                R.string.receipt_no to paymentDetailData.receiptNumber
            )

            list.also { emit(it) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun transaction(transaction: Transaction) {
        this.transaction.value = transaction
    }

    companion object
}