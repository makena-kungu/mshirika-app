package co.ke.mshirika.mshirika_app.data_layer.remote.models.request

import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil

@Keep
data class TransferFunds(
    val toOfficeId: Int,
    val toClientId: Int,
    val toAccountType: Int,
    val toAccountId: Int,
    val transferAmount: String,
    val transferDate: String,
    val transferDescription: String,
    val dateFormat: String = DateUtil.DATE_FORMAT,
    val locale: String = "en",
    val fromAccountId: String,
    val fromAccountType: String,
    val fromClientId: Int,
    val fromOfficeId: Int
)