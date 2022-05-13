package co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class GuarantorsTemplateWithClient(
    val guarantorType: GuarantorType,
    val status: Boolean,
    val accountLinkingOptions: List<AccountLinkingOption>
) : Respondent {
    @Keep
    @Parcelize
    data class GuarantorType(
        val id: Int,
        val code: String,
        val value: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class AccountLinkingOption(
        val id: Int,
        val accountNo: String,
        val clientId: Int,
        val clientName: String,
        val productId: Int,
        val productName: String,
        val fieldOfficerId: Int,
        val currency: Currency
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Currency(
            val code: String,
            val name: String,
            val decimalPlaces: Int,
            val inMultiplesOf: Int,
            val displaySymbol: String,
            val nameCode: String,
            val displayLabel: String
        ) : Parcelable
    }
}