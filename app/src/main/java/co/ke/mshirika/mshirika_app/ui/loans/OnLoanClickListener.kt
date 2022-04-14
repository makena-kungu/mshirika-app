package co.ke.mshirika.mshirika_app.ui.loans

import android.view.View
import co.ke.mshirika.mshirika_app.data.response.LoanAccount

interface OnLoanClickListener {
    fun onLoanClicked(loanAccount: LoanAccount)

    fun onLoanRepayClicked(loanAccount: LoanAccount, position: Int, container: View): Boolean
}