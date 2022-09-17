package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ClientTemplateConverter {
    val gson = Gson()


    @JvmStatic
    @TypeConverter
    fun toClientClassificationOption(s: String): List<ClientTemplate.ClientClassificationOption> {
        val type = object : TypeToken<List<ClientTemplate.ClientClassificationOption>>() {}.type
        return gson.fromJson(s, type)
    }

    @JvmStatic
    @TypeConverter
    fun fromClientClassificationOption(obj: List<ClientTemplate.ClientClassificationOption>): String {
        val type = object : TypeToken<List<ClientTemplate.ClientClassificationOption>>() {}.type
        return gson.toJson(obj, type)
    }

    @JvmStatic
    @TypeConverter
    fun toClientLegalFormOption(s: String): List<ClientTemplate.ClientLegalFormOption> {
        val type = object : TypeToken<List<ClientTemplate.ClientLegalFormOption>>() {}.type
        return gson.fromJson(s, type)
    }

    @JvmStatic
    @TypeConverter
    fun fromClientLegalFormOption(obj: List<ClientTemplate.ClientLegalFormOption>): String {
        val type = object : TypeToken<List<ClientTemplate.ClientLegalFormOption>>() {}.type
        return gson.toJson(obj, type)
    }

    @JvmStatic
    @TypeConverter
    fun toClientNonPersonConstitutionOption(s: String): List<ClientTemplate.ClientNonPersonConstitutionOption> {
        val type =
            object : TypeToken<List<ClientTemplate.ClientNonPersonConstitutionOption>>() {}.type
        return gson.fromJson(s, type)
    }

    @JvmStatic
    @TypeConverter
    fun fromClientNonPersonConstitutionOption(obj: List<ClientTemplate.ClientNonPersonConstitutionOption>): String {
        val type =
            object : TypeToken<List<ClientTemplate.ClientNonPersonConstitutionOption>>() {}.type
        return gson.toJson(obj, type)
    }

    @JvmStatic
    @TypeConverter
    fun toClientTypeOption(s: String): List<ClientTemplate.ClientTypeOption> {
        val type = object : TypeToken<List<ClientTemplate.ClientTypeOption>>() {}.type
        return gson.fromJson(s, type)
    }

    @JvmStatic
    @TypeConverter
    fun fromClientTypeOption(obj: List<ClientTemplate.ClientTypeOption>): String {
        val type = object : TypeToken<List<ClientTemplate.ClientTypeOption>>() {}.type
        return gson.toJson(obj, type)
    }

    @JvmStatic
    @TypeConverter
    fun toFamilyMemberOptions(s: String): ClientTemplate.FamilyMemberOptions {
        val type = object : TypeToken<ClientTemplate.FamilyMemberOptions>() {}.type
        return gson.fromJson(s, type)
    }

    @JvmStatic
    @TypeConverter
    fun fromFamilyMemberOptions(obj: ClientTemplate.FamilyMemberOptions): String {
        val type = object : TypeToken<ClientTemplate.FamilyMemberOptions>() {}.type
        return gson.toJson(obj, type)
    }

    @JvmStatic
    @TypeConverter
    fun toGenderOption(s: String): List<ClientTemplate.GenderOption> {
        val type = object : TypeToken<List<ClientTemplate.GenderOption>>() {}.type
        return gson.fromJson(s, type)
    }

    @JvmStatic
    @TypeConverter
    fun fromGenderOption(obj: List<ClientTemplate.GenderOption>): String {
        val type = object : TypeToken<List<ClientTemplate.GenderOption>>() {}.type
        return gson.toJson(obj, type)
    }

    @JvmStatic
    @TypeConverter
    fun toOfficeOption(s: String): List<ClientTemplate.OfficeOption> {
        val type = object : TypeToken<List<ClientTemplate.OfficeOption>>() {}.type
        return gson.fromJson(s, type)
    }

    @JvmStatic
    @TypeConverter
    fun fromOfficeOption(obj: List<ClientTemplate.OfficeOption>): String {
        val type = object : TypeToken<List<ClientTemplate.OfficeOption>>() {}.type
        return gson.toJson(obj, type)
    }

    @JvmStatic
    @TypeConverter
    fun toSavingProductOption(s: String): List<ClientTemplate.SavingProductOption> {
        val type = object : TypeToken<List<ClientTemplate.SavingProductOption>>() {}.type
        return gson.fromJson(s, type)
    }

    @JvmStatic
    @TypeConverter
    fun fromSavingProductOption(obj: List<ClientTemplate.SavingProductOption>): String {
        val type = object : TypeToken<List<ClientTemplate.SavingProductOption>>() {}.type
        return gson.toJson(obj, type)
    }

    @JvmStatic
    @TypeConverter
    fun toStaffOption(s: String): List<ClientTemplate.StaffOption> {
        val type = object : TypeToken<List<ClientTemplate.StaffOption>>() {}.type
        return gson.fromJson(s, type)
    }

    @JvmStatic
    @TypeConverter
    fun fromStaffOption(obj: List<ClientTemplate.StaffOption>): String {
        val type = object : TypeToken<List<ClientTemplate.StaffOption>>() {}.type
        return gson.toJson(obj, type)
    }
}