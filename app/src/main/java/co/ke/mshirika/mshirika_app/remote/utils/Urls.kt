package co.ke.mshirika.mshirika_app.remote.utils

import okhttp3.HttpUrl

object Urls {
    // TODO change the host  in production
    private const val HOST = "caritas.fiter.io"
    val BASE_URL: HttpUrl = HttpUrl.get("https://$HOST/fineract-provider/api/v1/")
}