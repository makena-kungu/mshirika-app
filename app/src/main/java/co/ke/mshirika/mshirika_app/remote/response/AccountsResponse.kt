package co.ke.mshirika.mshirika_app.remote.response

import co.ke.mshirika.mshirika_app.data.response.GuarantorAccount
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.SavingsAccount

data class AccountsResponse(
    val loanAccounts:List<LoanAccount>,
    val savingsAccounts:List<SavingsAccount>,
    val guarantorAccounts:List<GuarantorAccount>
)
