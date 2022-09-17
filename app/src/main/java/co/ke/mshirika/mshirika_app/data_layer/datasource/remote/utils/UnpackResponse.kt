package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils

import android.util.Log
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.DeveloperMessages.LOGIN
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome.Success
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


object UnpackResponse {

    @JvmStatic
    inline fun <T> respond(request: () -> Response<T>): Outcome<T> {
        var outcome: Outcome<T>? = null
        try {
            val response = request()
            outcome = response.handle
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
        return outcome ?: error()
    }

    @JvmStatic
    inline fun <T> respondWithSuccess(request: () -> Response<T>): T? {
        val response = respond(request)
        if (response !is Success) {
            return null
        }
        return response.data
    }

    @JvmStatic
    val <T> Response<T>.handle: Outcome<T>
        get() {
            if (isSuccessful) {
                return Success(body())
            }

            var msg: String? = null
            val code: Int = code()
            try {

                val responseError: ResponseBody? = errorBody()
                if (responseError == null) {
                    msg = "Unknown Error"
                    throw IllegalArgumentException("$code: $msg")
                }
                //extract error message from the above

                val gson = Gson()
                val body = responseError.string()
                val error: Error = gson.fromJson(body, Error::class.java)
                msg = error.errors.first().defaultUserMessage
                Log.d(TAG, "handle: error = $msg")
                if (msg == LOGIN) {
                    msg = "Wrong username or password!"
                }
                throw IllegalArgumentException("$code: $msg")
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

            return error(msg!!)
        }

    data class Error(
        val developerMessage: String,
        val httpStatusCode: String,
        val defaultUserMessage: String,
        val userMessageGlobalisationCode: String,
        val errors: List<Error>
    ) {
        data class Error(
            val developerMessage: String,
            val defaultUserMessage: String,
            val userMessageGlobalisationCode: String,
            val parameterName: String,
            val value: Any,
            val args: List<Arg>
        ) {
            data class Arg(
                val value: Int
            )
        }
    }

    private const val TAG = "UnpackResponse"
}
