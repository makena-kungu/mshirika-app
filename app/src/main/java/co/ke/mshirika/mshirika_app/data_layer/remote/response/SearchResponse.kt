package co.ke.mshirika.mshirika_app.data_layer.remote.response

import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Search
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
class SearchResponse : ArrayList<Search>(), Respondent