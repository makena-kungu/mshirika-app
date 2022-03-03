package co.ke.mshirika.mshirika_app.repositories

import co.ke.mshirika.mshirika_app.data.request.Login
import co.ke.mshirika.mshirika_app.data.response.Staff
import co.ke.mshirika.mshirika_app.remote.response.utils.UnpackResponse
import co.ke.mshirika.mshirika_app.remote.services.AuthService
import co.ke.mshirika.mshirika_app.utility.network.Result
import co.ke.mshirika.mshirika_app.utility.network.Result.Empty
import co.ke.mshirika.mshirika_app.utility.network.Result.Loading
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(
    private val authService: AuthService
) {

    val auth = MutableStateFlow<Result<Staff>>(Empty())

    suspend fun login(login: Login) {
        auth.value = Loading()
        UnpackResponse.respond {
            authService.login(login)
        }.also {
            auth.value = it
        }
    }
}