package co.ke.mshirika.mshirika_app.remote.response

import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.remote.utils.Feedback

class LoansResponse(
    pageItems: List<LoanAccount>,
    totalFilteredRecords: Int
) : Feedback<LoanAccount>(pageItems, totalFilteredRecords)