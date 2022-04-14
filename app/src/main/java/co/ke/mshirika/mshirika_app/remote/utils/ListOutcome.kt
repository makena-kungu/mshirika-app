package co.ke.mshirika.mshirika_app.remote.utils

sealed class ListOutcome<T> {
    class Empty<T> : ListOutcome<T>()
    class Loading<T> : ListOutcome<T>()
    data class Error<T>(val msg: String) : ListOutcome<T>()
    data class Success<T>(val data: MutableList<T>) : ListOutcome<T>()
}

fun <T> listError(msg: String = "Unknown Error") = ListOutcome.Error<T>(msg)
fun <T> listLoading() = ListOutcome.Loading<T>()
fun <T> listEmpty() = ListOutcome.Empty<T>()
fun <T> listSuccess(data: MutableList<T> = mutableListOf()) = ListOutcome.Success(data)