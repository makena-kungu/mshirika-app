package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.transfer_savings_or_shares

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.TransferFunds
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UNKNOWN_ERROR
import co.ke.mshirika.mshirika_app.data_layer.repositories.TransferFundsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.plainText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransferFundsViewModel @Inject constructor(
    private val repo: TransferFundsRepo
) : MshirikaViewModel() {

    suspend fun getTemplate(savingsAccountId: Int) = repo.getTemplate(savingsAccountId)

    suspend fun getTemplate(savingsAccountId: Int, toOffice: Int) =
        repo.getTemplate(savingsAccountId, toOffice)

    suspend fun getTemplate(client: Int, toOffice: Int, accountType: Int) =
        repo.getTemplate(client, toOffice, accountType)

    fun transfer(client: Client, tr: TransferSavingsOrShares) {
        val transfer = TransferFunds(
            toOfficeId = tr.officeId,
            toClientId = tr.clientId,
            toAccountType = tr.accountTypeId,
            toAccountId = tr.accountId,
            transferAmount = "${tr.amount}",
            transferDate = tr.date,
            transferDescription = tr.description,
            fromAccountId = client.savingsAccountId.toString(),
            fromAccountType = 2.toString(),
            fromClientId = client.id,
            fromOfficeId = client.officeId
        )
        viewModelScope.launch(Dispatchers.IO) {
            loadingChannel.send(true)
            val outcome = repo.transfer(transfer)
            loadingChannel.send(false)
            when (outcome) {
                is Outcome.Success -> successChannel.send(resourceText(R.string.transfer_successful))
                is Outcome.Error -> {
                    val text = when (val msg = outcome.msg) {
                        UNKNOWN_ERROR -> resourceText(R.string.transfer_error)
                        else -> plainText(msg)
                    }
                    errorChannel.send(text)
                }
                else -> {}
            }
        }
    }
}