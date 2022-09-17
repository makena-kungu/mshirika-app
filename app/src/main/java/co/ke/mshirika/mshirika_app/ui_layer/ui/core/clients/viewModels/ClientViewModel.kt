package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Transaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.SavingsAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.DetailedLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repo: ClientsRepo,
    private val store: PreferencesStoreRepository
) : MshirikaViewModel() {

    private val _client = Channel<Cliente>()
    private val _cuentas: Flow<AccountsResponse?> = _client.receiveAsFlow()
        .map {
            state[CLIENT_TAG] = it
            _ac.send(true)
            val accounts = repo.accounts(it.id)
            _ac.send(false)
            if (accounts is Outcome.Success) {
                val data = accounts.data ?: return@map null
                data.savingsAccounts
                data
            } else null
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())
    private val _cuentaDeAhorros = _cuentas.map { response ->
        response?.run { savingsAccounts.find { it.id == client.savingsAccountId } }
    }
    private val _prestamos = _cuentas.map { response -> //loans
        withContext(IO) {
            val loans = response?.loans?.filter { it.status.active }
            val list = mutableListOf<ConservativeLoanAccount>()
            if (loans == null) return@withContext list

            _lo.send(true)
            for (loan in loans) {
                Log.d(TAG, "loans: ${loan.productName}")
                val outcome = repo.loans(loan.id)
                if (outcome !is Outcome.Success) continue

                val data = outcome.data ?: continue
                list += data
            }
            _lo.send(false)
            list
        }
    }
    private val client: Cliente get() = state[CLIENT_TAG]!!

    private val _tr = Channel<Boolean>()
    private val _lo = Channel<Boolean>()
    private val _ac = Channel<Boolean>()
    private val loading =
        _tr.receiveAsFlow()
            .combineTransform(_lo.receiveAsFlow()) { transaction, loan ->
                emit(transaction || loan)
            }.combineTransform(_ac.receiveAsFlow()) { loansNTransaction, accounts ->
                emit(loansNTransaction || accounts)
            }

    val authKey: Flow<String?>
        get() = store.authKey
    val actas: LiveData<MutableList<Transaction>>
        get() = _cuentaDeAhorros.map { account ->
            Log.d(TAG, "transactions map: $account")
            val list = mutableListOf<Transaction>()
            if (account == null) return@map list
            withContext(IO) {
                _tr.send(true)
                val outcome = repo.transactions(accountId = account.id)
                _tr.send(false)
                if (outcome !is Outcome.Success) return@withContext list
                val data = outcome.data ?: return@withContext list
                val transactions = data.transactions ?: emptyList()
                list += transactions
                list
            }
        }.asLiveData()
    val cuentaDeAhorros: LiveData<SavingsAccount?>
        get() = _cuentaDeAhorros.asLiveData()
    val prestamos: LiveData<MutableList<ConservativeLoanAccount>>
        get() = _prestamos.asLiveData()

    suspend fun authKey() = authKey.first()

    fun client(client: Cliente) {
        state[CLIENT_TAG] = client
        viewModelScope.launch {
            _client.send(client)
        }
    }

    suspend fun getDetailedLoan(loanId: Int): DetailedLoanAccount? = withContext(IO) {
        repo.detailedLoans(loanId)
    }

    init {
        viewModelScope.launch {
            loading.collectLatest {
                loadingChannel.send(it)
            }
        }
    }

    companion object {
        private const val TAG = "ClientViewModel"
        private const val CLIENT_TAG = "client_tag"
    }
}