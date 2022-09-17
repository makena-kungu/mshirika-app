package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.loans

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate

@Entity(tableName = "new_loan_template")
data class OfflineNewLoanTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val clientId: Int,
    val template: NewLoanTemplate
)