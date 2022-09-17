package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity("office_table")
data class Office(
    @PrimaryKey
    val id: Int,
    val name: String,
    val nameDecorated: String,
    val externalId: String,
    @TypeConverters(co.ke.mshirika.mshirika_app.data_layer.datasource.local.Converter::class)
    val openingDate: List<Int>,
    val hierarchy: String,
    val parentId: Int,
    val parentName: String
) : Parcelable