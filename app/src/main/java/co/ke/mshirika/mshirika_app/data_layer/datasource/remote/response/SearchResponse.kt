package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Search
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
class SearchResponse : ArrayList<Search>(), Respondent