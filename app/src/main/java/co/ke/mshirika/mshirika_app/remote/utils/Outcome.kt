package co.ke.mshirika.mshirika_app.remote.utils

sealed class Outcome<T> {
    class Empty<T> : Outcome<T>()
    class Loading<T> : Outcome<T>()
    data class Error<T>(val msg: String) : Outcome<T>()
    data class Success<T>(val data: T?) : Outcome<T>()
}

fun <T> error(msg: String = "Unknown Error"): Outcome.Error<T> = Outcome.Error(msg)
fun <T> empty() = Outcome.Empty<T>()
fun <T> loading() = Outcome.Loading<T>()