package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable {

    @Parcelize
    data class Status(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    @Parcelize
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