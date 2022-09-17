package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.loans

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanRepaymentSchedule

@Entity(tableName = "loan_repayment_schedule")
data class OfflineLoanRepaymentSchedule(
    @PrimaryKey(autoGenerate = false)
    val loanId: Int,
    val clientId:Int,
    val schedule: LoanRepaymentSchedule
)
