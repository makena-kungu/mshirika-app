package co.ke.mshirika.mshirika_app.utility.network

import retrofit2.HttpException
import java.io.IOException

sealed class Outcome<T> {
    class Empty<T> : Outcome<T>()
    data class Error<T>(
        val msg: String? = "Unknown Error",
        val throwable: Throwable = Throwable(msg)
    ) : Outcome<T>()

    class Loading<T> : Outcome<T>()
    data class Success<T>(val data: T?) : Outcome<T>()
}

fun <T> Throwable.outcome(msg:String = "Unknown Error"): Outcome<T> {
    return when (this) {
        is HttpException -> message()
        is IOException -> message
        else -> null
    }?.let {
        Outcome.Error(it, this)
    } ?: Outcome.Error(msg, this)
}