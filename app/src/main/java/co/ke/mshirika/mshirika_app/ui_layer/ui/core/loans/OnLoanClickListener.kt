package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.view.View
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount

interface OnLoanClickListener {
    fun onLoanClicked(
        loanAccount: ConservativeLoanAccount,
        position: Int,
        container: View
    )

    fun onLoanRepayClicked(
        loanAccount: ConservativeLoanAccount,
        position: Int,
        container: View
    ): Boolean
}