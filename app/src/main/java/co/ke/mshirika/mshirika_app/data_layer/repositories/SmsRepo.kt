package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.ClientSms
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.ClientSmsResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import javax.inject.Inject

class SmsRepo @Inject constructor(
    private val cService: ClientsService,
    private val store: PreferencesStoreRepository
) {

    suspend fun sendSms(clientId: Int, message: String): Outcome<ClientSmsResponse> {
        val sms = ClientSms(clientId.toString(), message)
        return respond {
            cService.sendSms(store.headers(), sms)
        }
    }

}