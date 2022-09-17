package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.work_manager

import androidx.work.Data
import androidx.work.ListenableWorker.Result
import retrofit2.Response

object UnpackResponse {
    const val ERROR = "error"

    @JvmStatic
    suspend inline fun <T> response(
        crossinline request: suspend () -> Response<T>,
        success: (T) -> Unit
    ): Result {
        return try {
            val response = request()

            if (!response.isSuccessful) return Result.failure()
            val body = response.body() ?: return Result.success()

            success(body)
            Result.success()
        } catch (e: Exception) {
            val data = Data.Builder().putString(ERROR, e.message).build()
            Result.failure(data)
        }
    }
}