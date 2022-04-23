package co.ke.mshirika.mshirika_app.data.response

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Office(
    val id: Int,
    val name: String,
    val nameDecorated: String,
    val externalId: String,
    val openingDate: List<Int>,
    val hierarchy: String,
    val parentId: Int,
    val parentName: String
) : Parcelable