package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group

import androidx.room.TypeConverter
import com.google.gson.Gson

object GroupConverter {
    val gson = Gson()

    @JvmStatic
    @TypeConverter
    fun statusToJson(status: Grupo.Status): String {
        return gson.toJson(status)
    }

    @JvmStatic
    @TypeConverter
    fun fromJsonStatus(s: String): Grupo.Status {
        return gson.fromJson(s, Grupo.Status::class.java)
    }
}