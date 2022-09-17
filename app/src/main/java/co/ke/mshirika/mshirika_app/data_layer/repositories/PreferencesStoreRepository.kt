package co.ke.mshirika.mshirika_app.data_layer.repositories

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Staff
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.repositories.Keys.AUTH_KEY
import co.ke.mshirika.mshirika_app.data_layer.repositories.Keys.CLIENT_TEMPLATE_KEY
import co.ke.mshirika.mshirika_app.data_layer.repositories.Keys.OFFLINE_KEY
import co.ke.mshirika.mshirika_app.data_layer.repositories.Keys.STAFF_KEY
import com.google.android.gms.common.util.Base64Utils
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

private const val PREF_STORE = "p@Gh?aDM('Sy?AR=PDXqSS"
val Context.datastore by preferencesDataStore(name = PREF_STORE)

class PreferencesStoreRepository(private val context: Context) {
    private val gson = Gson()
    private val keySuffix = "cK(pkpT-ZLk)zq>IPoOl,wp#i"
    private val staffSuffix = "*s&X\$U)!NCgqk>xpq/psH"

    private val datastore get() = context.datastore

    suspend fun authKey(): String {
        datastore.data
        return datastore.data
            .map { preferences ->
                val key = preferences.key()!!
                key
            }.first()
    }

    private suspend fun saveAuthKey(key: String): Boolean {
        val authKey = Base64Utils.encode(key.toByteArray()) + keySuffix
        return datastore.edit {
            it[AUTH_KEY] = authKey
        }.let {
            it[AUTH_KEY] != null
        }
    }

    suspend fun saveStaff(staff: Staff): Boolean {
        val keySaved = saveAuthKey(staff.base64EncodedAuthenticationKey)
        val json = gson.toJson(staff)
        val encodedStaffJson = Base64Utils.encode(json.toByteArray()) + staffSuffix
        val staffSaved = datastore.edit {
            it[STAFF_KEY] = encodedStaffJson
        }.key() != null

        if (!staffSaved && keySaved) {
            deleteKeyAndStaff()
        }

        return staffSaved && keySaved
    }

    suspend fun staff(): Staff {
        return datastore.data.map {
            it.staff()!!
        }.last()
    }

    suspend fun syncAndGoOnline() {
        goOffline(false)
    }

    @JvmOverloads
    suspend fun goOffline(enableOfflineMode: Boolean = true) {
        datastore.edit {
            it[OFFLINE_KEY] = enableOfflineMode
        }
    }

    val isLoggedIn = datastore
        .data
        .map { it[AUTH_KEY]?.isBlank() == true }

    val authKey = datastore.data.map {
        it.key()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val isAuthed = authKey.mapLatest {
        it?.isBlank() != true
    }

    val staff = datastore.data.map {
        it.staff()
    }

    val isOffline = datastore.data.map {
        it[OFFLINE_KEY] ?: false
    }

    suspend fun logout() = deleteKeyAndStaff()

    private suspend fun deleteKeyAndStaff() =
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

    val clientTemplate
        get() = datastore.data.map {
            val json = it[CLIENT_TEMPLATE_KEY] ?: return@map null
            val gson = Gson()
            gson.fromJson(json, ClientTemplate::class.java)
        }

    suspend fun saveClientTemplate(clientTemplate: ClientTemplate) {
        datastore.edit {
            val gson = Gson()
            val json = gson.toJson(clientTemplate)
            it[CLIENT_TEMPLATE_KEY] = json
        }
    }

    companion object {
        private const val TAG = "PreferencesStoreReposit"
    }
}

private object Keys {
    val AUTH_KEY = stringPreferencesKey("-HejWbaR<jRt?jwN,yTkkWSfh")
    val STAFF_KEY = stringPreferencesKey("JLG\"NROgCAF's\$ZmoOuWt/")
    val OFFLINE_KEY = booleanPreferencesKey("is-offline")
    val CLIENT_TEMPLATE_KEY = stringPreferencesKey("client_template")
}