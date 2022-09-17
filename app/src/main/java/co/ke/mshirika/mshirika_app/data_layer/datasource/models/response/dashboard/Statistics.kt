package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.dashboard


import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.dashboard.Statistics.Statistic
import com.google.gson.annotations.SerializedName

class Statistics : ArrayList<Statistic>() {
    @Keep
    data class Statistic(
        @SerializedName(value = "count", alternate = ["lcount"])
        val count: Int,
        @SerializedName(value = "Weeks", alternate = ["Months", "Days"])
        val value: String
    )
}