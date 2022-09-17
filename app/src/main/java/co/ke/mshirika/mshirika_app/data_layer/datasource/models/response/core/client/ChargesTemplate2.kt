package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
data class ChargesTemplate2(
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
    val currencyOptions: List<CurrencyOption>,
    val chargeCalculationTypeOptions: List<ChargeCalculationTypeOption>,
    val chargeAppliesToOptions: List<ChargeAppliesToOption>,
    val chargeTimeTypeOptions: List<ChargeTimeTypeOption>,
    val chargePaymetModeOptions: List<ChargePaymetModeOption>,
    val loanChargeCalculationTypeOptions: List<LoanChargeCalculationTypeOption>,
    val loanChargeTimeTypeOptions: List<LoanChargeTimeTypeOption>,
    val savingsChargeCalculationTypeOptions: List<SavingsChargeCalculationTypeOption>,
    val savingsChargeTimeTypeOptions: List<SavingsChargeTimeTypeOption>,
    val clientChargeCalculationTypeOptions: List<ClientChargeCalculationTypeOption>,
    val clientChargeTimeTypeOptions: List<ClientChargeTimeTypeOption>,
    val shareChargeCalculationTypeOptions: List<ShareChargeCalculationTypeOption>,
    val shareChargeTimeTypeOptions: List<ShareChargeTimeTypeOption>,
    val feeFrequencyOptions: List<FeeFrequencyOption>,
    val incomeOrLiabilityAccountOptions: IncomeOrLiabilityAccountOptions,
    val taxGroupOptions: List<Any>
)  {
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
    data class CurrencyOption(
        val code: String,
        val name: String,
        val decimalPlaces: Int,
        val displaySymbol: String,
        val nameCode: String,
        val displayLabel: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ChargeCalculationTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ChargeAppliesToOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ChargeTimeTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ChargePaymetModeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class LoanChargeCalculationTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class LoanChargeTimeTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class SavingsChargeCalculationTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class SavingsChargeTimeTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ClientChargeCalculationTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ClientChargeTimeTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ShareChargeCalculationTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class ShareChargeTimeTypeOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class FeeFrequencyOption(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class IncomeOrLiabilityAccountOptions(
        val liabilityAccountOptions: List<LiabilityAccountOption>,
        val incomeAccountOptions: List<IncomeAccountOption>
    ) : Parcelable {
        @Keep
        @Parcelize
        data class LiabilityAccountOption(
            val id: Int,
            val name: String,
            val parentId: Int,
            val glCode: String,
            val disabled: Boolean,
            val manualEntriesAllowed: Boolean,
            val type: Type,
            val usage: Usage,
            val nameDecorated: String,
            val tagId: TagId,
            val description: String
        ) : Parcelable {
            @Keep
            @Parcelize
            data class Type(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable

            @Keep
            @Parcelize
            data class Usage(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable

            @Keep
            @Parcelize
            data class TagId(
                val id: Int,
                val active: Boolean,
                val mandatory: Boolean
            ) : Parcelable
        }

        @Keep
        @Parcelize
        data class IncomeAccountOption(
            val id: Int,
            val name: String,
            val parentId: Int,
            val glCode: String,
            val disabled: Boolean,
            val manualEntriesAllowed: Boolean,
            val type: Type,
            val usage: Usage,
            val description: String,
            val nameDecorated: String,
            val tagId: TagId
        ) : Parcelable {
            @Keep
            @Parcelize
            data class Type(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable

            @Keep
            @Parcelize
            data class Usage(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable

            @Keep
            @Parcelize
            data class TagId(
                val id: Int,
                val active: Boolean,
                val mandatory: Boolean
            ) : Parcelable
        }
    }
}