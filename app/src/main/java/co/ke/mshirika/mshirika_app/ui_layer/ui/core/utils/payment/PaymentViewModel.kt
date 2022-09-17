package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.payment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.CreateCharge
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.ChargesTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.ChargesTemplate2
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.DetailedSavingsAccount.Charge
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.ClientPaymentTemplate
import co.ke.mshirika.mshirika_app.data_layer.repositories.PaymentRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.utility.ld
import co.ke.mshirika.mshirika_app.utility.mld
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repo: PaymentRepo,
    private val store: PreferencesStoreRepository,
    private val useCase: PaymentUseCase
) : MshirikaViewModel() {

    private var _template: ClientPaymentTemplate?
        get() = state[TEMPLATE]
        set(value) {
            state[TEMPLATE] = value
        }

    private val _templateLd = mld<ClientPaymentTemplate>()
    val templateLd: ld<ClientPaymentTemplate>
        get() = _templateLd

    private var template: ChargesTemplate? = null
    private var template2: ChargesTemplate2? = null
    private var accountsResponse: AccountsResponse?
        get() = state[ACCOUNTS]
        set(value) {
            state[ACCOUNTS] = value
        }

    private val _charges = mld<List<Charge>>()
    val charges: ld<List<Charge>> = _charges

    val accounts by lazy { accountsResponse }

    suspend fun accounts(clientId: Int) = withContext(IO) {
        accountsResponse = repo.accounts(clientId)
        accountsResponse
    }

    fun account(savingsAccountId: Int) {
        viewModelScope.launch(IO) {
            val account = repo.account(savingsAccountId) ?: return@launch
            val charges = account.charges.filterNot { it.amountOutstanding == .0 }
            _charges.postValue(charges)
        }
    }

    fun pay(
        transactionDate: String,
        paymentType: String,
        bankDate: String,
        receiptNumber: String,
        deposits: List<PaymentDeposit>,
        loanRepayments: List<PaymentLoan>
    ) {
        val template = _template ?: return
        val paymentTypeId = template.paymentTypes.first { it.name == paymentType }.id
        viewModelScope.launch {
            useCase(
                transactionDate,
                paymentTypeId,
                bankDate,
                receiptNumber,
                deposits,
                loanRepayments
            )
        }
    }

    suspend fun charges(savingsAccountId: Int) = withContext(IO) {
        repo.charges(savingsAccountId)?.toList()?.let {
            it.filter { charge -> charge.amount == charge.amountPaid }
        }
    }

    suspend fun chargeTemplate(savingsAccountId: Int) = withContext(IO) {
        template = repo.queryChargeTemplate1(savingsAccountId)
        template
    }

    suspend fun chargeTemplate2(chargeId: Int) = withContext(IO) {
        template2 = repo.queryChargeTemplate2(chargeId)
        template2
    }

    fun queryTemplate(clientId: Int) {
        viewModelScope.launch(IO) {
            Log.d(TAG, "queryTemplate: client id = $clientId")
            template(clientId)
        }
    }

    private val clientId = MutableSharedFlow<Int>()

    fun clientId(cID: Int) {
        clientId.tryEmit(cID)
    }

    private val _templateFlow = clientId.combine(store.isOffline) { clientId, isSynced ->
        when (isSynced) {
            true -> repo.queryOfflineTemplate(clientId)
            false -> repo.queryTemplate(clientId)
        }
    }

    val templateFlow get() = _templateFlow.asLiveData()

    private suspend fun template(clientId: Int) {
        Log.d(TAG, "template: invoked")
        val isSynced = store.isOffline.first()
        Log.d(TAG, "template: is sync on? : ${if (isSynced) "Yes" else "No"}")

        when (isSynced) {
            true -> repo.queryOfflineTemplate(clientId)
            false -> repo.queryTemplate(clientId)
        }?.also { _templateLd.postValue(it) }
    }

    suspend fun temp(clientId: Int) = withContext(IO) {
        Log.d(TAG, "temp: invoked")
        repo.queryTemplate(clientId)
    }

    fun charge(savingsAccountId: Int, amount: Int) {
        val template2 = template2 ?: return
        account(savingsAccountId)
        viewModelScope.launch { repo.createCharge(savingsAccountId, CreateCharge(template2.id, amount)) }
    }

    companion object {
        const val TEMPLATE = "client_template"
        const val ACCOUNTS = "cuentas"
        private const val TAG = "PaymentViewModel"
    }
}