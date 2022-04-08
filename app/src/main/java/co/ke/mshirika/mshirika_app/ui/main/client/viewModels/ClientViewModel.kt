package co.ke.mshirika.mshirika_app.ui.main.client.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.Loan
import co.ke.mshirika.mshirika_app.data.response.SavingsAccount
import co.ke.mshirika.mshirika_app.pagingSource.Util.headers
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.amt
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
    private val authKey: String
) : ViewModel() {

    private val _client = MutableStateFlow<Client?>(null)
    private val _totalSavings = MutableStateFlow<String?>(null)

    private val _accounts get() = repo.accounts
    private val _loans
        get() = repo.loans

    private val headers: Map<String, String> by lazy {
        headers(authKey)
    }

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
        get() = repo.transactions


    fun reload() =
        viewModelScope.launch(IO) {
            client.collectLatest { client ->
                client?.let { repo.accounts(it.id, headers) }
            }
        }


    fun setClient(client: Client) {
        viewModelScope.launch(IO) {
            _client.value = client
            repo.apply {
                client.apply {
                    accounts(id, headers)
                    loans(id, headers)
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
                    accountId = it.id,
                    headers = headers
                )
            }
        }
    }

    private fun List<Loan>.loans() {
        viewModelScope.launch(IO) {
            forEach {
                repo.loans(it.id, headers)
            }
        }
    }

    init {
        viewModelScope.launch(IO) {
            _accounts.collectLatest { outcome ->
                if (outcome is Success)
                    outcome.data?.apply {
                        //do some calculations
                        savingsAccounts.savingAccounts()
                        loans.loans()
                    }
            }
        }
    }
}