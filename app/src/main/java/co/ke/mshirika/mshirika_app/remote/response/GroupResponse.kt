package co.ke.mshirika.mshirika_app.remote.response


import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.remote.utils.Feedback
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupResponse(
    override val pageItems: List<Group>,
    override val totalFilteredRecords: Int = 0
) : Feedback<Group>(pageItems, totalFilteredRecords)