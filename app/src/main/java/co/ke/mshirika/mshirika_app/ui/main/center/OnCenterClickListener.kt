package co.ke.mshirika.mshirika_app.ui.main.center

import android.view.View
import co.ke.mshirika.mshirika_app.data.response.Center

interface OnCenterClickListener {
    fun onClickCenter(center: Center, position: Int, view: View)
}