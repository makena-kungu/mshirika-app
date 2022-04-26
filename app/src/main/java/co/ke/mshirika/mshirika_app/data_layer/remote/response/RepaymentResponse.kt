package co.ke.mshirika.mshirika_app.data_layer.remote.response

import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.RepaymentType
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
class RepaymentResponse : ArrayList<RepaymentType>(), Respondent