package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Login
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Staff
import co.ke.mshirika.mshirika_app.data_layer.remote.services.AuthService
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome.Empty
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome.Loading
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(
    private val service: AuthService,
    private val pref:PreferencesStoreRepository
) {

    private var call: Call<Staff>? = null
    val auth = MutableStateFlow<Outcome<Staff>>(Empty())

    suspend fun login(login: Login) {
        auth.value = Loading()
        auth.value = respond {
            service.login(login)
        }
    }

    fun cancel() {
        call?.cancel()
    }

    suspend fun save(key: String) {
        pref.saveAuthKey(key)
    }

    suspend fun save(staff: Staff) {
        pref.saveStaff(staff)
    }
}