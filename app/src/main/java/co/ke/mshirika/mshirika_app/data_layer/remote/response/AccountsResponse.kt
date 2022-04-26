package co.ke.mshirika.mshirika_app.data_layer.remote.response

import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.GuarantorAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Loan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.SavingsAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountsResponse(
    val loans:List<Loan>,
    val savingsAccounts:List<SavingsAccount>,
    val guarantorAccounts:List<GuarantorAccount>
):Respondent
