package co.ke.mshirika.mshirika_app.ui.main.client.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.utility.network.Outcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientsRepo: ClientsRepo,
    private val authKey: Flow<String>
) : ViewModel() {

    private val _client = MutableStateFlow<Client?>(null)
    private val _repaymentSchedule = MutableSharedFlow<MutableMap<String, LoanRepaymentSchedule>>()

    private val headers: Map<String, String> by lazy {
        val map = mutableMapOf(
            "Fineract-Platform-TenantId" to "default"
        )
        viewModelScope.launch {
            authKey.collectLatest {
                map["Authorization"] = it
            }
        }
        map
    }

    val accounts = clientsRepo.accounts
    val client = _client.asSharedFlow()
    val repaymentSchedule = _repaymentSchedule.asSharedFlow()

    fun reload() =
        viewModelScope.launch(IO) {
            client.collectLatest { client ->
                client?.let { clientsRepo.loadClientAccounts(it, headers) }
            }
        }


    fun setClient(client: Client) {
        viewModelScope.launch(IO) {
            _client.emit(client)
            clientsRepo::loadClientAccounts
        }
    }

    suspend fun query(loanAccount: LoanAccount): Outcome<LoanRepaymentSchedule> {
        clientsRepo.queryRepaymentPeriod(loanAccount, headers)
    }

    fun save(schedule: LoanRepaymentSchedule) =
        viewModelScope.launch(IO) {
            _repaymentSchedule.collectLatest {
                it[schedule.accountNo] = schedule
                _repaymentSchedule.emit(it)
            }
        }
}