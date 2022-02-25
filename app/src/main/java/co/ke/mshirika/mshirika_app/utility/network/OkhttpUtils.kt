package co.ke.mshirika.mshirika_app.utility.network

import com.google.gson.Gson
import okhttp3.Response

object OkhttpUtils {
    val gson = Gson()

    inline fun <reified R> Response.toObj(): R? =
        if (isSuccessful)
            gson.fromJson(body()?.string(), R::class.java)
        else null
}