package co.ke.mshirika.mshirika_app.data.response

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    val id: Int,
    val accountNo: String,
    val externalId: String,
    val status: Status,
    val subStatus: SubStatus,
    val active: Boolean,
    val activationDate: List<Int>,
    val fullname: String?,
    val displayName: String,
    val mobileNo: String?,
    val dateOfBirth: List<Int>,
    val gender: Gender,
    val clientType: ClientType,
    val clientClassification: ClientClassification,
    val isStaff: Boolean,
    val officeId: Int,
    val officeName: String?,
    val staffId: Int,
    val staffName: String?,
    val timeline: Timeline,
    val savingsAccountId: Int,
    val legalForm: LegalForm,
) : Parcelable {

    @Parcelize
    data class Status(
        val id: Int,
        val code: String,
        val value: String,
    ) : Parcelable

    @Parcelize
    data class SubStatus(
        val id: Int,
        val name: String,
        val description: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Parcelize
    data class Gender(
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Parcelize
    data class ClientType(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Parcelize
    data class ClientClassification(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Parcelize
    data class Timeline(
        val submittedOnDate: List<Int>,
        val submittedByUsername: String,
        val submittedByFirstname: String,
        val submittedByLastname: String,
        val activatedOnDate: List<Int>,
        val activatedByUsername: String,
        val activatedByFirstname: String,
        val activatedByLastname: String
    ) : Parcelable

    @Parcelize
    data class LegalForm(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    companion object  : DiffUtil.ItemCallback<Client>() {
            override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean =
                oldItem == newItem
    }
}