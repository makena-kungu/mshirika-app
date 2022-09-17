package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.loans

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.DetailedLoanAccount

@Entity(tableName = "detailed_loans_table")
data class OfflineDetailedLoan(
    @PrimaryKey(autoGenerate = false)
    val loanId: Int,
    val clientId: Int,
    val loanAccount: DetailedLoanAccount
)
