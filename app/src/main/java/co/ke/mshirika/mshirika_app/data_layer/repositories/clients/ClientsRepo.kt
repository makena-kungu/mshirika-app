package co.ke.mshirika.mshirika_app.data_layer.repositories.clients

import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddBeneficiary
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddFam
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.client.AddNok
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.DetailedLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.EditClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.AccountsResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.ClientCreationResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.TransactionResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Feedback
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ClientsRepo {
    val created: StateFlow<Outcome<ClientCreationResponse>>
    val clients: Flow<PagingData<Cliente>>
    val searchedClients: Flow<PagingData<Cliente>>

    suspend fun accounts(clientId: Int): Outcome<AccountsResponse>
    suspend fun addNok(
        clientId: Int,
        addNok: AddNok
    ): CreateNok?

    suspend fun addBeneficiary(
        clientId: Int,
        beneficiary: AddBeneficiary
    ): CreateBeneficiary?

    suspend fun beneficiaries(clientId: Int): Beneficiary?
    fun cancel()
    suspend fun client(clientId: Int): DetailedClient?
    suspend fun createClient(client: CreateClient): ClientCreationResponse?
    suspend fun detailedLoans(loanId: Int): DetailedLoanAccount?
    suspend fun family(clientId: Int): Family?
    suspend fun editClient(client: CreateClient): EditClientResponse?
    suspend fun editClientTemplate(clientId: Int):EditClientTemplate?
    suspend fun loans(loanId: Int): Outcome<ConservativeLoanAccount>
    suspend fun nok(clientId: Int): NextOfKin?
    suspend fun transactions(accountId: Int): Outcome<TransactionResponse>
    suspend fun template(): ClientTemplate?
    suspend fun search(query: String): Feedback<Cliente>?

    val clientes: Flow<PagingData<Cliente>>
}