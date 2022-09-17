package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.Login
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Staff
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.AuthService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respond
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val service: AuthService,
    private val pref: PreferencesStoreRepository
) {

    suspend fun login(login: Login) = withContext(IO) {
        respond {
            service.login(login)
        }
    }

    suspend fun save(staff: Staff) =
        withContext(IO) {
            pref.saveStaff(staff)
        }
}