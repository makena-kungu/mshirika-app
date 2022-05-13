package co.ke.mshirika.mshirika_app.data_layer.remote.response

import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.GuarantorAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.LoanFromClientAccounts
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.SavingsAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountsResponse(
    @SerializedName("loanAccounts")
    val loans:List<LoanFromClientAccounts>,
    val savingsAccounts:List<SavingsAccount>,
    val guarantorAccounts:List<GuarantorAccount>
):Respondent
