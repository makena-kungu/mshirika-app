package co.ke.mshirika.mshirika_app.ui_layer.ui.loans

import android.view.View
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount

interface OnLoanClickListener {
    fun onLoanClicked(loanAccount: LoanAccount)

    fun onLoanRepayClicked(loanAccount: LoanAccount, position: Int, container: View): Boolean
}