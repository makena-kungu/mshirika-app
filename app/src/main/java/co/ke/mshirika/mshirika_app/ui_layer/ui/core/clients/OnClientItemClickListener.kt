package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients

import android.view.View
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client

interface OnClientItemClickListener {
    fun onClickClient(
        containerView: View,
        client: Client,
        imageUrl: String? = null,
        colors: IntArray? = null
    )
}