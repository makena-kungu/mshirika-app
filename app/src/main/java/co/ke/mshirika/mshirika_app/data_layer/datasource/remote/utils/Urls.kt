package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils

import okhttp3.HttpUrl

object Urls {
    private const val HOST = ""
    @JvmStatic
    val BASE_URL: HttpUrl = HttpUrl.get("https://$HOST/fineract-provider/api/v1/")
}