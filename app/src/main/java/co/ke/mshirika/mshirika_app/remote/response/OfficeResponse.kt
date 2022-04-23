package co.ke.mshirika.mshirika_app.remote.response


import co.ke.mshirika.mshirika_app.data.response.Office
import co.ke.mshirika.mshirika_app.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
class OfficeResponse : ArrayList<Office>(), Respondent