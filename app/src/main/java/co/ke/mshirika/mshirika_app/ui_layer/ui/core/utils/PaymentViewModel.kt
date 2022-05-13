package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils

import android.util.Log
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.LoanFromClientAccounts
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.SavingsAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.response.ClientPaymentTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.empty
import co.ke.mshirika.mshirika_app.data_layer.repositories.Other
import co.ke.mshirika.mshirika_app.data_layer.repositories.PaymentRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PaymentViewModel"

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repo: PaymentRepo
) : MshirikaViewModel() {

    private val _template = MutableStateFlow<Outcome<ClientPaymentTemplate>>(empty())
    val template = flow {
        _template.collectLatest { outcome ->
            outcome.stateHandler {
                emit(it)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun client(client: Client) {
        viewModelScope.launch {
            _template.value = repo.queryTemplate(client.id)
        }
    }

    fun pay(
        paymentMode: String,
        shares: Triple<SavingsAccount, Double, Double>?,
        loans: MutableMap<LoanFromClientAccounts, Pair<Double, Double>>,
        other: Other
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "pay: init")

            template.last()?.let { template ->
                val modeId = template.paymentTypes.first { it.name == paymentMode }.id
                Log.d(TAG, "pay: modeid = $modeId")

                repo.pay(shares, loans, other.copy(modeId = modeId))
            }
        }
    }
}