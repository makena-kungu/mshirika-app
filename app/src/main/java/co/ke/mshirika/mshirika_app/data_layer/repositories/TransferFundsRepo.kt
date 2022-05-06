package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.TransferFunds
import co.ke.mshirika.mshirika_app.data_layer.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransferFundsRepo @Inject constructor(
    private val service: ClientsService,
    private val store: PreferencesStoreRepository
) {

    suspend fun getTemplate(fromAccountId: Int) = withContext(IO) {
        respondWithSuccess {
            service.transferFundsTemplate(
                store.headers(),
                fromAccountId = fromAccountId,
            )
        }
    }

    suspend fun getTemplate(fromAccountId: Int, toOffice: Int) = withContext(IO) {
        respondWithSuccess {
            service.transferFundsTemplateWithToClients(
                store.headers(),
                fromAccountId = fromAccountId,
                toOfficeId = toOffice
            )
        }
    }

    suspend fun getTemplate(fromAccountId: Int, toOffice: Int, accountType: Int) = withContext(IO) {
        respondWithSuccess {
            service.transferFundsTemplateWithAccounts(
                store.headers(),
                fromAccountId = fromAccountId,
                toOfficeId = toOffice,
                toAccountType = accountType
            )
        }
    }

    suspend fun transfer(transfer: TransferFunds) = withContext(IO) {
        respond {
            service.transferFunds(store.headers(), transfer)
        }
    }
}