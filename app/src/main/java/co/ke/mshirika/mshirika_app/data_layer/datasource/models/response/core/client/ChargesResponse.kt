package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import androidx.annotation.Keep

class ChargesResponse : ArrayList<ChargesResponse.Charge>(){
    @Keep
    data class Charge(
        val id: Int,
        val chargeId: Int,
        val accountId: Int,
        val name: String,
        val chargeTimeType: ChargeTimeType,
        val dueDate: List<Int>,
        val chargeCalculationType: ChargeCalculationType,
        val percentage: Int,
        val amountPercentageAppliedTo: Int,
        val currency: Currency,
        val amount: Double,
        val amountPaid: Double,
        val amountWaived: Int,
        val amountWrittenOff: Int,
        val amountOutstanding: Double,
        val amountOrPercentage: Double,
        val penalty: Boolean,
        val isActive: Boolean
    ) {
        @Keep
        data class ChargeTimeType(
            val id: Int,
            val code: String,
            val value: String
        )
    
        @Keep
        data class ChargeCalculationType(
            val id: Int,
            val code: String,
            val value: String
        )
    
        @Keep
        data class Currency(
            val code: String,
            val name: String,
            val decimalPlaces: Int,
            val displaySymbol: String,
            val nameCode: String,
            val displayLabel: String
        )
    }
}