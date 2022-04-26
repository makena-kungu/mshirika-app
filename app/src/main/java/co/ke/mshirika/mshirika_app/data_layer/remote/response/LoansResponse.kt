package co.ke.mshirika.mshirika_app.data_layer.remote.response

import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Feedback

class LoansResponse(
    pageItems: List<LoanAccount>,
    totalFilteredRecords: Int
) : Feedback<LoanAccount>(pageItems, totalFilteredRecords)