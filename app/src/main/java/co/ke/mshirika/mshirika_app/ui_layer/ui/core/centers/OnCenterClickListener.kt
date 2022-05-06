package co.ke.mshirika.mshirika_app.ui_layer.ui.core.centers

import android.view.View
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Center

interface OnCenterClickListener {
    fun onClickCenter(center: Center, position: Int, view: View)
}