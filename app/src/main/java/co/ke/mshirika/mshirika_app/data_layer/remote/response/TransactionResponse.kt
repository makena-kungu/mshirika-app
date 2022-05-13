package co.ke.mshirika.mshirika_app.data_layer.remote.response


import android.os.Parcelable
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Transaction
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.common.Currency
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionResponse(
    val transactions: List<Transaction>
) : Respondent