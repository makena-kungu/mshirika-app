package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils

import co.ke.mshirika.mshirika_app.R

sealed class Outcome<T> {
    class Empty<T> : Outcome<T>()
    class Loading<T> : Outcome<T>()
    data class Error<T>(val msg: String) : Outcome<T>()
    data class Success<T>(val data: T? = null) : Outcome<T>()
}

fun <T> error(msg: String = UNKNOWN_ERROR): Outcome.Error<T> = Outcome.Error(msg)
fun <T> empty() = Outcome.Empty<T>()
fun <T> loading() = Outcome.Loading<T>()

fun <T> Outcome.Error<T>.resource(default: Int): Int = when (msg) {
    NETWORK_ERROR -> R.string.network_error
    CANCELLATION_ERROR -> R.string.request_cancelled
    else -> default
}

const val UNKNOWN_ERROR = "Unknown Error"
const val CANCELLATION_ERROR = "Cancellation Error"
const val NETWORK_ERROR = "network error"