package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response


import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Transaction

data class TransactionResponse(
    val transactions: List<Transaction>?
)