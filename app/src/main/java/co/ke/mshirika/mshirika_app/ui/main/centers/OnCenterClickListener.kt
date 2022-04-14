package co.ke.mshirika.mshirika_app.ui.main.centers

import android.view.View
import co.ke.mshirika.mshirika_app.data.response.Center

interface OnCenterClickListener {
    fun onClickCenter(center: Center, position: Int, view: View)
}