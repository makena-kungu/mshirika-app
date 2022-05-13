package co.ke.mshirika.mshirika_app.data_layer.remote.utils

import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.LoanRepaymentSchedule

interface GetsRepaymentSchedule {
    suspend fun repaymentSchedule(loanId: Int): LoanRepaymentSchedule?
}