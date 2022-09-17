package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.FamilyMember
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity("create_clients_table")
@TypeConverters(CreateClient.CreateClientConverter::class)
data class CreateClient(
    @PrimaryKey(autoGenerate = false) @Expose(serialize = false) val id: Int,
    val externalId: String,
    val officeId: Int,
    val legalFormId: Int? = null,
    val isStaff: Boolean,
    val active: Boolean = true,
    val genderId: Int,
    val dateOfBirth: String,
    val clientTypeId: Int,
    val clientClassificationId: Int,
    val submittedOnDate: String = mshirikaDate,
    val firstname: String,
    val middlename: String,
    val lastname: String,
    val activationDate: String = mshirikaDate,
    val savingsProductId: Int,
    val familyMembers: List<FamilyMember>,
    val dateFormat: String = DateUtil.DATE_FORMAT,
    val locale: String = "en",
    val address: List<String> = emptyList()
) : Parcelable {

    @Keep
    @Parcelize
    data class FamilyMember(
        val firstName: String,
        val lastName: String,
        val age: String,
        val relationshipId: Int,
        val genderId: Int,
        val maritalStatusId: Int,
        val dateOfBirth: String,
        val dateFormat: String = DateUtil.DATE_FORMAT,
        val locale: String = "en"
    ) : Parcelable

    object CreateClientConverter {
        @TypeConverter
        @JvmStatic
        fun familyMembers(familyMembers: List<FamilyMember>): String {
            return Gson().toJson(familyMembers)
        }

        @TypeConverter
        @JvmStatic
        fun familyMembers(json: String): List<FamilyMember> {
            val type = object : TypeToken<List<FamilyMember>>() {}.type
            return Gson().fromJson(json, type)
        }

        @TypeConverter
        @JvmStatic
        fun address(list: List<String>): String {
            return Gson().toJson(list)
        }

        @TypeConverter
        @JvmStatic
        fun address(json: String): List<String> {
            val type = object : TypeToken<List<String>>() {}.type
            return Gson().fromJson(json, type)
        }
    }
}