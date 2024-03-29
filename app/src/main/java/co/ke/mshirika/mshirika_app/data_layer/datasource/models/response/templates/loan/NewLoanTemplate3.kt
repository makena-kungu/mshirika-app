package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class NewLoanTemplate3(
    val canDisburse: Boolean,
    val isInterestRecalculationEnabled: Boolean,
    val isVariableInstallmentsAllowed: Boolean,
    val isEqualAmortization: Boolean,
    val isRatesEnabled: Boolean
) : Parcelable