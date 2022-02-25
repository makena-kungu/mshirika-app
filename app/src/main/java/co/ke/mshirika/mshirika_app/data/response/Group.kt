package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val active: Boolean,
    val hierarchy: String,
    val id: Int,
    val name: String,
    val officeId: Int,
    val officeName: String,
    val status: Status
) : Parcelable {

    @Parcelize
    data class Status(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    companion object : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean =
            oldItem == newItem

    }
}