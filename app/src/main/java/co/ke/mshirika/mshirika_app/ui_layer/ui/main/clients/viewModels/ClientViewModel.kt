package co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.viewModels

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Loan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.SavingsAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.amt
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
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

    /*
    * get() = repo.accounts.map {
            when (it) {
                is Outcome.Empty -> {
                    loadingChannel.send(false)
                    null
                }
                is Outcome.Error -> {
                    errorChannel.send(UIText.PlainText(it.msg))
                    loadingChannel.send(false)
                    null
                }
                is Outcome.Loading -> {
                    loadingChannel.send(true)
                    null
                }
                is Outcome.Success -> {
                    loadingChannel.send(false)
                    it.data
                }
            }

    * */
    val loans
        get() = _loans.asStateFlow()
    val client
        get() = _client.asSharedFlow()
    val totalSavings
        get() = _totalSavings.asStateFlow()
    val savings
        get() = _accounts.map { outcome ->
            when (outcome) {
                is Outcome.Success -> outcome.data?.savingsAccounts?.reduce { acc, savingsAccount ->
                    acc.copy(accountBalance = acc.accountBalance + savingsAccount.accountBalance)
                }?.accountBalance ?: .0
                else -> .0
            }
        }
    val transactions
        get() = _transactions.asStateFlow()

    suspend fun authKey() = prefRepo.authKey()

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
        // todo this method sums up all the savings accounts but queries the transactions for the
        //      main account only
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
                //do some calculations
                it.savingsAccounts.savingAccounts()
                it.loans.loans()
            }) {
                reload()
            }
        }
    }

    private suspend fun handleTransactions() {
        repo.transactions.collectLatest { outcome ->
            outcome.stateHandler {
                _transactions.value = it
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