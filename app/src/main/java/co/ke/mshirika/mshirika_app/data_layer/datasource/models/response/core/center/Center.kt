package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center


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
@Entity(tableName = "centers_table")
data class Center(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val accountNo: String,
    @TypeConverters(co.ke.mshirika.mshirika_app.data_layer.datasource.local.Converter::class)
    val activationDate: List<Int>,
    val active: Boolean,
    val externalId: String,
    val hierarchy: String,
    val name: String,
    val officeId: Int,
    val officeName: String,
    @TypeConverters(CenterConverter::class)
    val status: Status,
    @TypeConverters(CenterConverter::class)
    val timeline: Timeline
) : Respondent {

    @Parcelize
    @Keep
    data class Status(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
    @Keep
    data class Timeline(
        val activatedByFirstname: String,
        val activatedByLastname: String,
        val activatedByUsername: String,
        val activatedOnDate: List<Int>,
        val submittedByFirstname: String,
        val submittedByLastname: String,
        val submittedByUsername: String,
        val submittedOnDate: List<Int>
    ) : Parcelable

    companion object : DiffUtil.ItemCallback<Center>() {
        override fun areItemsTheSame(oldItem: Center, newItem: Center): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Center, newItem: Center): Boolean {
            return oldItem == newItem
        }
    }
}