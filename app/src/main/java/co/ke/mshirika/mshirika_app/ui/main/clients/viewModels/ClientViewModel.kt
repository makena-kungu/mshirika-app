package co.ke.mshirika.mshirika_app.ui.main.clients.viewModels

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.Loan
import co.ke.mshirika.mshirika_app.data.response.SavingsAccount
import co.ke.mshirika.mshirika_app.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.ui.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui.util.ViewUtils.amt
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val repo: ClientsRepo,
    private val prefRepo: PreferencesStoreRepository
) : MshirikaViewModel() {

    private val _client = MutableStateFlow<Client?>(null)
    private val _totalSavings = MutableStateFlow<String?>(null)

    private val _accounts get() = repo.accounts
    private val _transactions = MutableStateFlow<TransactionResponse?>(null)
    private val _loans
        get() = repo.loans

    //they are showing a snackbar
    val accounts
        get() = _accounts.asSharedFlow()
    val loans
        get() = _loans
    val client
        get() = _client.asSharedFlow()
    val totalSavings
        get() = _totalSavings.asStateFlow()
    val transactions
        get() = _transactions.asStateFlow()

    suspend fun authKey()  = prefRepo.authKey()

    fun reload() =
        viewModelScope.launch(IO) {
            client.collectLatest { client ->
                client?.let { repo.accounts(it.id) }
            }
        }


    fun setClient(client: Client) {
        viewModelScope.launch(IO) {
            _client.value = client
            repo.apply {
                client.apply {
                    accounts(id)
                    loans(id)
                }
            }
        }
    }

    private fun List<SavingsAccount>.savingAccounts() {
        sumOf { account ->
            account.accountBalance.toBigDecimal()
        }.also { sum ->
            _totalSavings.value = sum.amt
        }
        find {
            it.productName.contains("shares", ignoreCase = true)
        }?.also {
            viewModelScope.launch(IO) {
                repo.transactions(
                    accountId = it.id
                )
            }
        }
    }

    private fun List<Loan>.loans() {
        viewModelScope.launch(IO) {
            forEach {
                repo.loans(it.id)
            }
        }
    }

    private suspend fun handleAccounts() {
        _accounts.collectLatest { outcome ->
            outcome.stateHandlerWithAction("Retry", success = {
                data?.run {
                    //do some calculations
                    savingsAccounts.savingAccounts()
                    loans.loans()
                }
            }) {
                reload()
            }
        }
    }

    private suspend fun handleTransactions() {
        repo.transactions.collectLatest { outcome ->
            outcome.stateHandler {
                data?.let {
                    _transactions.value = it
                }
            }
        }
    }

    init {
        viewModelScope.launch(IO) {
            handleAccounts()
            handleTransactions()
        }
    }
}