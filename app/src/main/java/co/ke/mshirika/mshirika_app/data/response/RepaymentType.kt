package co.ke.mshirika.mshirika_app.data.response

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class RepaymentType(
    val description: String,
    val id: Int,
    val isCashPayment: Boolean,
    val name: String,
    val position: Int
) : Parcelable