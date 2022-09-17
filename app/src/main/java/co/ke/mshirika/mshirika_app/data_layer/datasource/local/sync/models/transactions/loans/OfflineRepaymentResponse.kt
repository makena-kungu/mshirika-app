package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.loans

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.RepaymentResponse

@Entity("repayment_type")
data class OfflineRepaymentResponse @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val repaymentTypes: RepaymentResponse
)
