package co.ke.mshirika.mshirika_app.remote.response

import co.ke.mshirika.mshirika_app.data.response.RepaymentType
import co.ke.mshirika.mshirika_app.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
class RepaymentResponse : ArrayList<RepaymentType>(), Respondent