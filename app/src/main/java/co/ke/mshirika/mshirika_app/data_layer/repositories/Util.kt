package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Util {
    suspend fun PreferencesStoreRepository.headers() = withContext(Dispatchers.IO) {
        authKey().headers
    }
}