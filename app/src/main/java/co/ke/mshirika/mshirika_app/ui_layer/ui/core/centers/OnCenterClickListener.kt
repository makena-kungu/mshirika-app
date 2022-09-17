package co.ke.mshirika.mshirika_app.ui_layer.ui.core.centers

import android.view.View
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.Center

interface OnCenterClickListener {
    fun onClickCenter(center: Center, position: Int, view: View)
}