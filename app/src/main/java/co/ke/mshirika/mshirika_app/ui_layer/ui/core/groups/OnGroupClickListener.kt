package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups

import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group

interface OnGroupClickListener {
    fun onClickGroup(group: Group, position: Int)
}