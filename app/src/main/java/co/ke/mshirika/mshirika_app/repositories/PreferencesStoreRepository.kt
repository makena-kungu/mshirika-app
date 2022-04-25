package co.ke.mshirika.mshirika_app.utility

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import co.ke.mshirika.mshirika_app.data.response.Staff
import co.ke.mshirika.mshirika_app.data.response.Staff.Companion.staff
import co.ke.mshirika.mshirika_app.data.response.Staff.Companion.string
import co.ke.mshirika.mshirika_app.utility.Keys.AUTH_KEY
import co.ke.mshirika.mshirika_app.utility.Keys.STAFF_KEY
import com.google.android.gms.common.util.Base64Utils
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

private const val PREF_STORE = "preferences"
val Context.datastore by preferencesDataStore(name = PREF_STORE)

class PreferencesStoreRepository(private val context: Context) {

    suspend fun authKey(): String {
        return context.datastore.data
            .map {
                it[AUTH_KEY]!!.let { key ->
                    "Basic ${Base64Utils.decode(key).decodeToString()}"
                }
            }.last()
    }

    suspend fun saveAuthKey(key: String): Boolean {
        val authKey = Base64Utils.encode(key.toByteArray())
        return context.datastore.edit {
            it[AUTH_KEY] = authKey
        }.let {
            it[AUTH_KEY] != null
        }
    }

    suspend fun saveStaff(staff: Staff) {
        val s = Base64Utils.encode(staff.string().toByteArray())
        context.datastore.edit {
            it[STAFF_KEY] = s
        }
    }

    suspend fun staff(): Staff {
        val encoded = context.datastore.data.map { it[STAFF_KEY] }.last()!!
        val staff = Base64Utils.decode(encoded).decodeToString()
        return staff.staff()
    }

    val isLoggedIn = context.datastore
        .data
        .map { !it[AUTH_KEY].isNullOrBlank() }

    suspend fun logout() =
        context.datastore.edit {
            removeAll(AUTH_KEY, STAFF_KEY)
        }

    private suspend fun removeAll(vararg preferences: Preferences.Key<*>) {
        context.datastore.edit {
            preferences.forEach { key ->
                it.remove(key)
            }
        }
    }
}

private object Keys {
    val AUTH_KEY = stringPreferencesKey("abcd")
    val STAFF_KEY = stringPreferencesKey("oxpsgwo")
}