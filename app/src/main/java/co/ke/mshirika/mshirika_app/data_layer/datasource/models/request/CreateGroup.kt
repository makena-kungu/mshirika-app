package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.DATE_FORMAT
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity("create_group_table")
data class CreateGroup (
    @PrimaryKey @Expose(serialize = false) val id:Int,
    val officeId: String,
    val name: String,
    val externalId: String,
    val dateFormat: String = DATE_FORMAT,
    val locale: String = "en",
    val active: Boolean,
    val activationDate: String? = null,
    val submittedOnDate: String
) : Parcelable