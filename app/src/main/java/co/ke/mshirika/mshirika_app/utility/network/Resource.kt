package co.ke.mshirika.mshirika_app.utility.network

import retrofit2.HttpException
import java.io.IOException


class Resource<out T>(val status: Status?, val data: T?, val message: String?) {
    companion object {

        fun <T> success(data: T?) = Resource<T>(Status.SUCCESS, data, null)

        fun <T> error(): Resource<T> = error("Unknown Error")

        fun <T> error(msg: String?): Resource<T> = msg?.let {
            Resource<T>(Status.ERROR, null, msg)
        } ?: error()

        fun <T> loading() = Resource<T>(Status.LOADING, null, null)

        fun <T> empty() = Resource<T>(null, null, null)
    }
}

/**
 * Determines the appropriate msg to show wrt the given Throwable
 */
fun <T> Throwable.resource(): Resource<T> {
    return when (this) {
        is HttpException -> message()
        is IOException -> message
        else -> null
    }?.let {
        Resource.error(it)
    } ?: Resource.error()
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}