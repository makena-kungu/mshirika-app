package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Search(
    val entityAccountNo: String,
    val entityExternalId: String,
    val entityId: Int,
    val entityMobileNo: String,
    val entityName: String,
    val entityNationalId: String,
    val entityStatus: EntityStatus,
    val entityType: String,
    val parentId: Int,
    val parentName: String,
    val parentType: String
) : Parcelable {

    @Parcelize
    data class EntityStatus(
        val code: String,
        val id: Int,
        val value: String
    ) : Parcelable

    companion object {
        const val ENTITY_CLIENT = "CLIENT"
    }
}