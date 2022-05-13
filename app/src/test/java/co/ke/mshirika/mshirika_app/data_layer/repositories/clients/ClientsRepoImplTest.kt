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
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class ClientsRepoImplTest : ClientsRepo {
    override val created: StateFlow<Outcome<ClientCreationResponse>>
        get() = TODO("Not yet implemented")
    override val clients: Flow<PagingData<Client>>
        get() = TODO("Not yet implemented")
    override val searchedClients: Flow<PagingData<Client>>
        get() = TODO("Not yet implemented")

    override suspend fun accounts(clientId: Int): Outcome<AccountsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun loans(loanId: Int): Outcome<ConservativeLoanAccount> {
        TODO("Not yet implemented")
    }

    override suspend fun search(query: String): Feedback<Client>? {
        TODO("Not yet implemented")
    }

    override suspend fun createClient(client: CreateClient): ClientCreationResponse? {
        val gson = Gson()
        val json = gson.toJson(client)
        println("json = \n$json")
        return null
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override suspend fun transactions(accountId: Int): Outcome<TransactionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun template(): ClientTemplate? {
        TODO("Not yet implemented")
    }
}