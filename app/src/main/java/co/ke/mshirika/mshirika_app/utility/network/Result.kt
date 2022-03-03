package co.ke.mshirika.mshirika_app.utility.network

sealed class Result<T> {
    class Empty<T> : Result<T>()
    class Loading<T> : Result<T>()
    data class Error<T>(val msg: String) : Result<T>()
    data class Success<T>(val data: T?) : Result<T>()

    companion object {
        fun <T> error(msg: String = "Unknown Error"): Error<T> = Error(msg)
    }
}