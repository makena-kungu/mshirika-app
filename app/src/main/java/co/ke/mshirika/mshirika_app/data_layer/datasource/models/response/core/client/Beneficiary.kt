package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mediumDate
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Keep
@Parcelize
data class Beneficiary(
    @SerializedName("data")
    val data: List<Data>
) : Respondent {
    @Keep
    @Parcelize
    data class Data(
        @SerializedName("rowWithCodeLookupValues")
        val values: List<String?>
    ) : Parcelable {
        val id get() = values[0]?.toInt()
        val name get() = values[2]
        val relationship get() = values[3]
        val phoneNumber get() = values[4]
        val nationalId get() = values[5]
        val dob get() = values[6].date
        val note get() = values[7]
        val percentageAllocation get() = values[8]

        companion object :DiffUtil.ItemCallback<Data>(){
            private const val FORMAT = "yyyy-MM-dd"

            private val String?.date: String?
                get() {
                    val sdf = SimpleDateFormat(FORMAT, Locale.getDefault())
                    return this?.let { sdf.parse(it)?.run { time.mediumDate } }
                }

            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem == newItem
            }
        }
    }
}