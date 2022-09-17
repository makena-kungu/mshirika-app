package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.loans

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate2

@Entity(tableName = "new_loan_template_1")
data class OfflineNewLoanTemplate2(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val clientId: Int,
    val productName: String,
    val newLoanTemplate: NewLoanTemplate2
)
