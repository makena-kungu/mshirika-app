package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity("create_centers_table")
@TypeConverters(CreateCenter.Converter::class)
data class CreateCenter constructor(
    @PrimaryKey(autoGenerate = true) @Expose(serialize = false) val id: Int,
    val name: String,
    val officeId: Int,
    val submittedOnDate: String,
    val staffId: String = "",
    val externalId: String,
    val active: Boolean = true,
    val activationDate: String = System.currentTimeMillis().mshirikaDate,
    val locale: String = "en",
    val dateFormat: String = DateUtil.DATE_FORMAT,
    val groupMembers: List<String> = emptyList()
) : Parcelable {

    constructor(
        name: String,
        officeId: Int,
        submittedOnDate: String,
        staffId: String = "",
        externalId: String,
        active: Boolean = true,
        activationDate: String = System.currentTimeMillis().mshirikaDate,
        locale: String = "en",
        dateFormat: String = DateUtil.DATE_FORMAT,
        groupMembers: List<String> = emptyList()
    ) : this(
        0,
        name,
        officeId,
        submittedOnDate,
        staffId,
        externalId,
        active,
        activationDate,
        locale,
        dateFormat,
        groupMembers
    )

    object Converter {

        @TypeConverter
        fun groupMembers(json: String): List<String> {
            return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
        }

        @TypeConverter
        fun groupMembers(members: List<String>): String {
            return Gson().toJson(members)
        }
    }
}