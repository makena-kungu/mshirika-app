package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client

import androidx.room.TypeConverter
import com.google.gson.Gson

object ClientConverter {
    val gson = Gson()

    @JvmStatic
    @TypeConverter
    fun statusToJson(status: Cliente.Status): String {
        return gson.toJson(status)
    }

    @JvmStatic
    @TypeConverter
    fun jsonToStatus(json: String): Cliente.Status {
        return gson.fromJson(json, Cliente.Status::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun subStatusToJson(subStatus: Cliente.SubStatus): String {
        return gson.toJson(subStatus)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToSubStatus(json: String): Cliente.SubStatus {
        return gson.fromJson(json, Cliente.SubStatus::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun genderToJson(status: Cliente.Gender): String {
        return gson.toJson(status)
    }

    @JvmStatic
    @TypeConverter
    fun jsonToGender(json: String): Cliente.Gender {
        return gson.fromJson(json, Cliente.Gender::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun clientTypeToJson(status: Cliente.ClientType): String {
        return gson.toJson(status)
    }

    @JvmStatic
    @TypeConverter
    fun jsonToClientType(json: String): Cliente.ClientType {
        return gson.fromJson(json, Cliente.ClientType::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun clientClassificationToJson(status: Cliente.ClientClassification): String {
        return gson.toJson(status)
    }

    @JvmStatic
    @TypeConverter
    fun jsonToClientClassification(json: String): Cliente.ClientClassification {
        return gson.fromJson(json, Cliente.ClientClassification::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun timelineToJson(status: Cliente.Timeline): String {
        return gson.toJson(status)
    }

    @JvmStatic
    @TypeConverter
    fun jsonToTimeline(json: String): Cliente.Timeline {
        return gson.fromJson(json, Cliente.Timeline::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun legalFormToJson(status: Cliente.LegalForm): String {
        return gson.toJson(status)
    }

    @JvmStatic
    @TypeConverter
    fun jsonToLegalForm(json: String): Cliente.LegalForm {
        return gson.fromJson(json, Cliente.LegalForm::class.java)
    }
}