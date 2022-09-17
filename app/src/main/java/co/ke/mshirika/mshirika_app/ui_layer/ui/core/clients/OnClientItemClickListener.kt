package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients

import android.view.View
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente

interface OnClientItemClickListener {
    fun onClickClient(
        containerView: View,
        client: Cliente,
        imageUrl: String? = null,
        colors: IntArray? = null
    )
}