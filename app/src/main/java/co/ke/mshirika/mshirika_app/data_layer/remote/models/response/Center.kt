package co.ke.mshirika.mshirika_app.data_layer.remote.models.response


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class Center(
    val accountNo: String,
    val activationDate: List<Int>,
    val active: Boolean,
    val externalId: String,
    val hierarchy: String,
    val id: Int,
    val name: String,
    val officeId: Int,
    val officeName: String,
    val status: Status,
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