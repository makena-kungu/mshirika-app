package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo

interface OnGroupClickListener {
    fun onClickGroup(group: Grupo, position: Int)
}