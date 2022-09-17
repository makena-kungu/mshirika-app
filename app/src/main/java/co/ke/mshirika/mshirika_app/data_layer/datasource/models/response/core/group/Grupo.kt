package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
@Entity(tableName = "groups_table")
data class Grupo(
    @PrimaryKey
    val id: Int,
    val active: Boolean,
    val hierarchy: String,
    val name: String,
    val officeId: Int,
    val officeName: String,
    @TypeConverters(GroupConverter::class)
    val status: Status
) : Respondent {

    @Parcelize
    @Keep
    data class Status(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    companion object : DiffUtil.ItemCallback<Grupo>() {
        override fun areItemsTheSame(oldItem: Grupo, newItem: Grupo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Grupo, newItem: Grupo): Boolean =
            oldItem == newItem

    }
}