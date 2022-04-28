package co.ke.mshirika.mshirika_app.data_layer.repositories

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Staff
import co.ke.mshirika.mshirika_app.data_layer.repositories.Keys.AUTH_KEY
import co.ke.mshirika.mshirika_app.data_layer.repositories.Keys.STAFF_KEY
import com.google.android.gms.common.util.Base64Utils
import com.google.gson.Gson
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

private const val PREF_STORE = "p@Gh?aDM('Sy?AR=PDXqSS"
val Context.datastore by preferencesDataStore(name = PREF_STORE)

class PreferencesStoreRepository(private val context: Context) {
    private val gson = Gson()
    private val keySuffix = "cK(pkpT-ZLk)zq>IPoOl,wp#i"
    private val staffSuffix = "*s&X\$U)!NCgqk>xpq/psH"

    private val datastore get() = context.datastore

    suspend fun authKey(): String {
        return datastore.data
            .map { preferences ->
                val key = preferences.key()!!
                key
            }.last()
    }

    suspend fun saveAuthKey(key: String): Boolean {
        val authKey = Base64Utils.encode(key.toByteArray()) + keySuffix
        return datastore.edit {
            it[AUTH_KEY] = authKey
        }.let {
            it[AUTH_KEY] != null
        }
    }

    suspend fun saveStaff(staff: Staff) {
        val json = gson.toJson(staff)
        val s = Base64Utils.encode(json.toByteArray()) + staffSuffix
        datastore.edit {
            it[STAFF_KEY] = s
        }
    }

    suspend fun staff(): Staff {
        return datastore.data.map {
            it.staff()!!
        }.last()
    }

    val isLoggedIn = datastore
        .data
        .map { it[AUTH_KEY]?.isBlank() == true }

    val authKey = datastore.data.map {
        it.key()
    }

    val staff = datastore.data.map {
        it.staff()
    }

    suspend fun logout() =
        datastore.edit {
            removeAll(AUTH_KEY, STAFF_KEY)
        }

    private fun Preferences.key(): String? {
        return this[AUTH_KEY]?.let { encodedKey ->
            val encodeAuthKey = encodedKey.removeSuffix(keySuffix)
            "Basic ${Base64Utils.decode(encodeAuthKey).decodeToString()}"
        }
    }

    private fun Preferences.staff(): Staff? {
        var encodedJson = this[STAFF_KEY]
        encodedJson = encodedJson?.removeSuffix(staffSuffix)
        val json = Base64Utils.decode(encodedJson).decodeToString()
        return gson.fromJson(json, Staff::class.java)
    }

    private suspend fun removeAll(vararg preferences: Preferences.Key<*>) {
        datastore.edit {
            preferences.forEach { key ->
                it.remove(key)
            }
        }
    }
}

private const val TAG = "PreferencesStoreReposit"

private object Keys {
    val AUTH_KEY = stringPreferencesKey("-HejWbaR<jRt?jwN,yTkkWSfh")
    val STAFF_KEY = stringPreferencesKey("JLG\"NROgCAF's\$ZmoOuWt/")
}