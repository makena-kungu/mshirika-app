package co.ke.mshirika.mshirika_app.remote.response


import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.remote.response.utils.Feedback
import kotlinx.parcelize.Parcelize

@Parcelize
data class CenterResponse(
    override val pageItems: List<Center>,
    override val totalFilteredRecords: Int = 0
) : Feedback<Center>(pageItems, totalFilteredRecords)