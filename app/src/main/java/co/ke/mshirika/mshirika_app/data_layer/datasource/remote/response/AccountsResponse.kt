package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response

import android.os.Parcelable
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.SavingsAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.GuarantorAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanFromClientAccounts
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountsResponse(
    @SerializedName("loanAccounts")
    val loans:List<LoanFromClientAccounts>,
    val savingsAccounts:List<SavingsAccount>,
    val guarantorAccounts:List<GuarantorAccount>
):Parcelable,Respondent
