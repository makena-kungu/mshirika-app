package co.ke.mshirika.mshirika_app.remote.response

import co.ke.mshirika.mshirika_app.data.response.GuarantorAccount
import co.ke.mshirika.mshirika_app.data.response.Loan
import co.ke.mshirika.mshirika_app.data.response.SavingsAccount
import co.ke.mshirika.mshirika_app.remote.response.utils.Respondent

data class AccountsResponse(
    val loans:List<Loan>,
    val savingsAccounts:List<SavingsAccount>,
    val guarantorAccounts:List<GuarantorAccount>
):Respondent
