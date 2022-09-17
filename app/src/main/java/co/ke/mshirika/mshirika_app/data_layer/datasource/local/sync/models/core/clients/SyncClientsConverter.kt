package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.clients

import androidx.room.TypeConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Transaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.AccountsResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SyncClientsConverter {

    @TypeConverter
    @JvmStatic
    fun fromTransactions(obj: List<Transaction>): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toTransactions(str: String): List<Transaction> {
        val gson = Gson()
        val type = object : TypeToken<List<Transaction>>() {}.type
        return gson.fromJson(str, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromTemplate(obj: ClientTemplate): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toTemplate(str: String): ClientTemplate {
        val gson = Gson()
        return gson.fromJson(str, ClientTemplate::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun fromAccounts(obj: AccountsResponse): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toAccounts(str: String): AccountsResponse {
        val gson = Gson()
        return gson.fromJson(str, AccountsResponse::class.java)
    }
}