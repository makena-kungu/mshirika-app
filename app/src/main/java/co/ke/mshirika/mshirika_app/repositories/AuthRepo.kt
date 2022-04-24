package co.ke.mshirika.mshirika_app.repositories

import co.ke.mshirika.mshirika_app.data.request.Login
import co.ke.mshirika.mshirika_app.data.response.Staff
import co.ke.mshirika.mshirika_app.remote.services.AuthService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Empty
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Loading
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse
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
        /*respond {
            service.login(login)
        }.also {
            auth.value = it
        }*/

        auth.value = UnpackResponse.respondWithCallback {
            service.loginWithCall(login).also { call = it }
        }
    }

    fun cancel() {
        call?.cancel()
    }

    suspend fun save(key: String) {
        pref.saveAuthKey(key)
    }
}