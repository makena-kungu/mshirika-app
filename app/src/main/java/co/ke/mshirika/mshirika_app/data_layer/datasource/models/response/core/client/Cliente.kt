package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client

import android.graphics.Color
import android.os.Parcelable
import android.util.Log
import androidx.annotation.Keep
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.math.sqrt

@Parcelize
@Keep
@Entity(tableName = "clients_table")
@TypeConverters(ClientConverter::class)
data class Cliente(
    @PrimaryKey val id: Int,
    val accountNo: String,
    @SerializedName("externalId")
    val memberNumber: String?,
    val status: Status,
    val subStatus: SubStatus,
    val active: Boolean,
    val activationDate: List<Int>,
    val fullname: String?,
    val displayName: String,
    val mobileNo: String?,
    val dateOfBirth: List<Int>?,
    val gender: Gender,
    val clientType: ClientType,
    val clientClassification: ClientClassification?,
    val isStaff: Boolean,
    val officeId: Int,
    val officeName: String?,
    val staffId: Int,
    val staffName: String?,
    val timeline: Timeline,
    val savingsAccountId: Int,
    val legalForm: LegalForm,
    @Expose(deserialize = false)
    val color: Int = color(),
    @Expose(deserialize = false)
    val hasImage: Boolean = false
) : Respondent {

    @Parcelize
    @Keep
    data class Status(
        val id: Int,
        val code: String?,
        val value: String?,
    ) : Parcelable

    @Parcelize
    @Keep
    data class SubStatus(
        val id: Int,
        val name: String?,
        val description: String?,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Parcelize
    @Keep
    data class Gender(
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Parcelize
    @Keep
    data class ClientType(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Parcelize
    @Keep
    data class ClientClassification(
        val id: Int,
        val name: String,
        val active: Boolean,
        val mandatory: Boolean
    ) : Parcelable

    @Parcelize
    @Keep
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
    @Keep
    data class LegalForm(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    companion object : DiffUtil.ItemCallback<Cliente>() {
        override fun areItemsTheSame(oldItem: Cliente, newItem: Cliente): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Cliente, newItem: Cliente): Boolean =
            oldItem == newItem

        private const val WHITE = Color.WHITE
        private const val TAG = "Cliente"

        @JvmStatic
        fun color(): Int {
            var color: Int
            do {
                color = generateColor()
                val hsv = FloatArray(3)
                Color.colorToHSV(color, hsv)
                val hasEnoughContrast = contrast(color)
                val isBright = bright(color)
            } while (!hasEnoughContrast && !isBright)

            Log.d(TAG, "color: $color")
            return color
        }

        @JvmStatic
        fun contrast(color: Int): Boolean {
            val contrast = ColorUtils.calculateContrast(color, WHITE)
            return contrast >= .45
        }

        @JvmStatic
        fun bright(color: Int): Boolean {
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)

            val brightness = sqrt(red * red * .241 + green * green * .691 + blue * blue * .068).toInt()
            return brightness >= 190
        }

        @JvmStatic
        fun generateColor(): Int {
            val red = rgb()
            val blue = rgb()
            val green = rgb()

            return Color.rgb(red, green, blue)
        }

        @JvmStatic
        val Int.background: Int
            get() {
                val color = this
                val r = Color.red(color)
                val g = Color.green(color)
                val b = Color.blue(color)

                return Color.argb(40, r, g, b)
            }

        @JvmStatic
        fun rgb(): Int {
            return (0..255).random()
        }
    }
}
