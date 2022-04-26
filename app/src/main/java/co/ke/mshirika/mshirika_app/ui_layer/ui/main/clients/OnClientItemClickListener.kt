package co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients

import android.view.View
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client

interface OnClientItemClickListener {
    fun onClickClient(containerView: View, client: Client, imageUrl: String, colors: IntArray)
}