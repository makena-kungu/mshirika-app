package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ChargesTemplate(
    val percentage: Int,
    val amountPercentageAppliedTo: Int,
    val amount: Int,
    val amountPaid: Int,
    val amountWaived: Int,
    val amountWrittenOff: Int,
    val amountOutstanding: Int,
    val amountOrPercentage: Int,
    val penalty: Boolean,
    val chargeOptions: List<ChargeOption>
) : Parcelable {
    @Keep
    @Parcelize
    data class ChargeOption(
        val id: Int,
        val name: String,
        val active: Boolean,
        val penalty: Boolean,
        val currency: Currency,
        val amount: Double,
        val chargeTimeType: ChargeTimeType,
        val chargeAppliesTo: ChargeAppliesTo,
        val chargeCalculationType: ChargeCalculationType,
        val chargePaymentMode: ChargePaymentMode,
        val feeOnMonthDay: List<Int>,
        val feeInterval: Int,
        val feeFrequency: FeeFrequency
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Currency(
            val code: String,
            val name: String,
            val decimalPlaces: Int,
            val displaySymbol: String,
            val nameCode: String,
            val displayLabel: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ChargeTimeType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ChargeAppliesTo(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ChargeCalculationType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ChargePaymentMode(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class FeeFrequency(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable
    }
}