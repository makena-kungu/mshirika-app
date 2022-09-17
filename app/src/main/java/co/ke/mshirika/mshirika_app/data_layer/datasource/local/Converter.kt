package co.ke.mshirika.mshirika_app.data_layer.datasource.local

import androidx.room.TypeConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.common.Currency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converter {


    @JvmStatic
    @TypeConverter
    fun dateToLong(date: List<Int>?): String? {
        if (date == null) {
            return null
        }
        return Gson().toJson(date)
        //return date.date
    }

    @JvmStatic
    @TypeConverter
    fun longToDate(json: String?): List<Int>? {
        if (json == null) {
            return null
        }
        return Gson().fromJson(json, object : TypeToken<List<Int>>() {}.type)
    }

    @JvmStatic
    @TypeConverter
    fun toCurrent(json: String): Currency {
        return Gson().fromJson(json, Currency::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun fromCurrent(obj: Currency): String {
        return Gson().toJson(obj)
    }
}