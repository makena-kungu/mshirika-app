package co.ke.mshirika.mshirika_app.utility

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import co.ke.mshirika.mshirika_app.utility.Keys.AUTH_KEY
import com.google.android.gms.common.util.Base64Utils
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

private const val PREF_STORE = "preferences"
val Context.datastore by preferencesDataStore(name = PREF_STORE)

class PreferencesStoreRepository(private val context: Context) {

    suspend fun saveAuthKey(authKey: String): Boolean {
        return context.datastore.edit {
            it[AUTH_KEY] = authKey
        }.let {
            it[AUTH_KEY] != null
        }
    }

    val isLoggedIn = context.datastore
        .data
        .map { !it[AUTH_KEY].isNullOrBlank() }

    suspend fun authKey(): String {
        return context.datastore.data
            .map {
                it[AUTH_KEY]!!
            }.run {
                val key = last()
                "Basic ${Base64Utils.decode(key).decodeToString()}"
            }
    }

    suspend fun logout() =
        context.datastore.edit {
            it.remove(AUTH_KEY)
        }
}

private object Keys {
    val AUTH_KEY = stringPreferencesKey("abcd")
}