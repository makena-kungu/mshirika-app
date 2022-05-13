package co.ke.mshirika.mshirika_app.data_layer.repositories.clients

import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.response.ClientCreationResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Feedback
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ClientsRepo {
    val created: StateFlow<Outcome<ClientCreationResponse>>
    val clients: Flow<PagingData<Client>>
    val searchedClients: Flow<PagingData<Client>>

    suspend fun accounts(clientId: Int): Outcome<AccountsResponse>
    suspend fun loans(loanId: Int): Outcome<ConservativeLoanAccount>
    suspend fun search(query: String): Feedback<Client>?
    suspend fun createClient(client: CreateClient): ClientCreationResponse?
    fun cancel()
    suspend fun transactions(accountId: Int): Outcome<TransactionResponse>
    suspend fun template(): ClientTemplate?
}