package co.ke.mshirika.mshirika_app.ui.main.group

import co.ke.mshirika.mshirika_app.data.response.Group

interface OnGroupClickListener {
    fun onClickGroup(group: Group, position: Int)
}