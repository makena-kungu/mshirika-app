package co.ke.mshirika.mshirika_app.utility

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.common.util.Base64Utils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

//todo change this into a singleton and allow for dependency injection
@Singleton
class DataStore(private val context: Context) {

    suspend fun saveAuthKey(authKey: String): Boolean {
        return context.datastore.edit {
            it[AUTH_KEY] = authKey
        }.let {
            it[AUTH_KEY] != null
        }
    }

    val isLoggedIn = context.datastore.data.map { !it[AUTH_KEY].isNullOrBlank() }
    val authKey = context.datastore.data.map { Base64Utils.decode(it[AUTH_KEY]).decodeToString() }

    suspend fun authKey(): String? {
        var authKey: String? = null
        //todo authKey = context.datastore.data.last()[AUTH_KEY]
        context.datastore.data.collectLatest {
            it[AUTH_KEY]?.let { key ->
                authKey = "Basic ${Base64Utils.decode(key).decodeToString()}"
            }
        }

        return authKey
    }

    suspend fun logout() =
        context.datastore.edit {
            it.remove(AUTH_KEY)
        }

    companion object {
        private const val PREF_STORE = "mshirikadatastore"
        private val AUTH_KEY = stringPreferencesKey("abcd")

        val Context.datastore by preferencesDataStore(name = PREF_STORE)
    }
}