package co.ke.mshirika.mshirika_app.data_layer.remote.response


import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Feedback
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupResponse(
    override val pageItems: List<Group>,
    override val totalFilteredRecords: Int = 0
) : Feedback<Group>(pageItems, totalFilteredRecords)