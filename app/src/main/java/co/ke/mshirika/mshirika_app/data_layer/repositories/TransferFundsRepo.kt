package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.ClientDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.TransferFunds
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransferFundsRepo @Inject constructor(
    private val service: ClientsService,
    private val store: PreferencesStoreRepository,
    private val dao: ClientDao
) {

    suspend fun getTemplateOffline(fromAccountId: Int) = withContext(IO) {
        dao.getOfflineTransferFundsTemplate(fromAccountId)
    }

    suspend fun getTemplate(fromAccountId: Int) = withContext(IO) {
        respondWithSuccess {
            service.transferFundsTemplate(
                store.headers(),
                fromAccountId = fromAccountId,
            )
        }
    }

    suspend fun getTemplateWithToClients(fromAccountId: Int) = withContext(IO) {
        dao.getOfflineTransferFundsWithToClientsTemplate(fromAccountId)
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

    suspend fun getTemplateWithToClientsAccounts(fromAccountId: Int) =
        withContext(IO) {
            dao.getOfflineTransferFundsTemplateWithToClientsAccounts(fromAccountId)
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

    suspend fun transferOffline(transfer: TransferFunds) = withContext(IO) {
        dao.insert(transfer)
    }

    suspend fun transfer(transfer: TransferFunds) = withContext(IO) {
        respond {
            service.transferFunds(store.headers(), transfer)
        }
    }
}