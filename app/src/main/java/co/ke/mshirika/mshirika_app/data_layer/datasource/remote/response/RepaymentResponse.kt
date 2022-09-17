package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.RepaymentType
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
class RepaymentResponse : ArrayList<RepaymentType>(), Respondent