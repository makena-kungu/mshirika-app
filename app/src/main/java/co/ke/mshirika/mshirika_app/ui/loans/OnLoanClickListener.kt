package co.ke.mshirika.mshirika_app.ui.loans

import co.ke.mshirika.mshirika_app.data.response.LoanAccount

interface OnLoanClickListener {
    fun onLoanClicked(loanAccount: LoanAccount)
}