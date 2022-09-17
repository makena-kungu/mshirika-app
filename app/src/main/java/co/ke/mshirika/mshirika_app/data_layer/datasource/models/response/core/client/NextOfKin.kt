package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class NextOfKin(
    @SerializedName("data")
    val data: List<Datum>
) : Respondent {
    @Keep
    @Parcelize
    data class Datum(
        @SerializedName("rowWithCodeLookupValues")
        val values: List<String>
    ) : Parcelable {

        val id get() = values[0].toInt()
        val name get() = values[2]
        val relationship get() = values[3]
        val nationalId get() = values[4]
        val phoneNumber get() = values[5]

        companion object : DiffUtil.ItemCallback<Datum>() {

            override fun areItemsTheSame(oldItem: Datum, newItem: Datum): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Datum, newItem: Datum): Boolean {
                return oldItem == newItem
            }
        }
    }
}