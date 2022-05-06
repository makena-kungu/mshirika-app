package co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent

@Keep
@Parcelize
data class NewLoanTemplate(
    val clientId: Int,
    val clientAccountNo: String,
    val clientName: String,
    val clientOfficeId: Int,
    val isLoanProductLinkedToFloatingRate: Boolean,
    val isFloatingInterestRate: Boolean,
    val timeline: Timeline,
    val productOptions: List<ProductOption>,
    val loanOfficerOptions: List<LoanOfficerOption>,
    val accountLinkingOptions: List<AccountLinkingOption>,
    val canDisburse: Boolean,
    val isTopup: Boolean,
    val isInterestRecalculationEnabled: Boolean,
    val isVariableInstallmentsAllowed: Boolean,
    val isEqualAmortization: Boolean,
    val isRatesEnabled: Boolean
) : Respondent {
    @Keep
    @Parcelize
    data class Timeline(
        val expectedDisbursementDate: List<Int>
    ) : Parcelable

    @Keep
    @Parcelize
    data class ProductOption(
        val id: Int,
        val name: String,
        val includeInBorrowerCycle: Boolean,
        val useBorrowerCycle: Boolean,
        val isLinkedToFloatingInterestRates: Boolean,
        val isFloatingInterestRateCalculationAllowed: Boolean,
        val allowVariableInstallments: Boolean,
        val isInterestRecalculationEnabled: Boolean,
        val canDefineInstallmentAmount: Boolean,
        //val principalVariationsForBorrowerCycle: List<Any>,
        //val interestRateVariationsForBorrowerCycle: List<Any>,
        //val numberOfRepaymentVariationsForBorrowerCycle: List<Any>,
        val canUseForTopup: Boolean,
        val isRatesEnabled: Boolean,
        val multiDisburseLoan: Boolean,
        val holdGuaranteeFunds: Boolean,
        val accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean,
        val syncExpectedWithDisbursementDate: Boolean,
        val isEqualAmortization: Boolean
    ) : Parcelable

    @Keep
    @Parcelize
    data class LoanOfficerOption(
        val id: Int,
        val firstname: String,
        val lastname: String,
        val displayName: String,
        val mobileNo: String,
        val officeId: Int,
        val officeName: String,
        val isLoanOfficer: Boolean,
        val isActive: Boolean,
        val joiningDate: List<Int>
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