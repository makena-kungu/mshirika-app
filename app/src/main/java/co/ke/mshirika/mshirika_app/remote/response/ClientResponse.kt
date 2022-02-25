package co.ke.mshirika.mshirika_app.remote.response

import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.remote.response.roots.Feedback
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClientResponse(
    override val pageItems: List<Client>,
    override val totalFilteredRecords: Int = 0
) : Feedback<Client>(pageItems, totalFilteredRecords)