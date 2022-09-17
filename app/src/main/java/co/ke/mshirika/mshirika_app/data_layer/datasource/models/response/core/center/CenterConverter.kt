package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center

import androidx.room.TypeConverter
import com.google.gson.Gson

object CenterConverter {
    val gson = Gson()

    @JvmStatic
    @TypeConverter
    fun statusToJson(status: Center.Status): String {
        return gson.toJson(status)
    }

    @JvmStatic
    @TypeConverter
    fun fromJsonStatus(s: String): Center.Status {
        return gson.fromJson(s, Center.Status::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun statusToJson(timeline: Center.Timeline): String {
        return gson.toJson(timeline)
    }

    @JvmStatic
    @TypeConverter
    fun fromJsonTimeline(s: String): Center.Timeline {
        return gson.fromJson(s, Center.Timeline::class.java)
    }
}