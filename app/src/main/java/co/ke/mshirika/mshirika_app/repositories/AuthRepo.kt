package co.ke.mshirika.mshirika_app.repositories

import co.ke.mshirika.mshirika_app.data.request.Login
import co.ke.mshirika.mshirika_app.data.response.Staff
import co.ke.mshirika.mshirika_app.remote.services.AuthService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Empty
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Loading
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(
    private val authService: AuthService
) {

    val auth = MutableStateFlow<Outcome<Staff>>(Empty())

    suspend fun login(login: Login) {
        auth.value = Loading()
        respond {
            authService.login(login)
        }.also {
            auth.value = it
        }
    }
}