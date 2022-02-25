package co.ke.mshirika.mshirika_app.repositories

import co.ke.mshirika.mshirika_app.data.request.Login
import co.ke.mshirika.mshirika_app.data.response.Staff
import co.ke.mshirika.mshirika_app.remote.services.AuthService
import co.ke.mshirika.mshirika_app.utility.network.Outcome
import co.ke.mshirika.mshirika_app.utility.network.Outcome.*
import co.ke.mshirika.mshirika_app.utility.network.outcome
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(
    private val authService: AuthService
) {

    val auth = MutableStateFlow<Outcome<Staff>>(Empty())

    fun login(login: Login) {
        auth.value = Loading()
        try {
            authService.login(login).run {
                if (isSuccessful)
                    Success(body())
                else
                    errorBody()?.let { Error(it.string()) } ?: Error()
            }
        } catch (e: IOException) {
            //Error("Network Request Interrupted", e)
            e.outcome("Network Request Interrupted")
        } catch (e: HttpException) {
            //Error(e.message(), e)
            e.outcome(e.message())
        }.also {
            auth.value = it
        }
    }
}