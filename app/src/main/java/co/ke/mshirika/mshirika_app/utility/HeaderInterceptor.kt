package co.ke.mshirika.mshirika_app.utility

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val appContext: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return with(chain.request().newBuilder()) {
            addHeader("Fineract-Platform-TenantId", "default")

            //fetching the authorization key
            CoroutineScope(Dispatchers.IO + Job()).launch(Dispatchers.IO) {
                DataStore(appContext)
                    .authKey()?.let {
                        Log.d(TAG, "intercept: $it")
                        addHeader("Authorization", it)
                    }
            }
            build()
        }.let {
            chain.proceed(it)
        }

    }

    companion object {
        private const val TAG = "HeaderInterceptor"
    }
}