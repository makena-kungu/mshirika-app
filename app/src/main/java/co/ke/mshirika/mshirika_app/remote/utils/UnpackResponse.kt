package co.ke.mshirika.mshirika_app.remote.utils

import co.ke.mshirika.mshirika_app.remote.utils.DeveloperMessages.LOGIN
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Companion.error
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Success
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object UnpackResponse {
    inline fun <T : Respondent> respond(request: () -> Response<T>): Outcome<T> {
        var outcome: Outcome<T>? = null
        try {
            val response = request()
            outcome = response.handle()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        } catch (e: Exception) {
            //Any other error
            e.printStackTrace()
        }
        return outcome ?: error()
    }

    fun <T> Response<T>.handle(): Outcome<T> {
        if (isSuccessful) {
            return Success(body())
        }

        var msg: String? = null
        val code: Int = code()
        try {

            val error: ResponseBody? = errorBody()
            //extract error message from the above
            msg = error?.run {
                JSONObject(string()).run {
                    getString("developerMessage")
                }
            }

            if (msg == null) {
                msg = "Unknown Error"
            }

            msg = when (msg) {
                LOGIN -> "Wrong Username or Password"
                else -> msg
            }

            throw IllegalArgumentException("$code: $msg")
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        return error(msg!!)
    }
}