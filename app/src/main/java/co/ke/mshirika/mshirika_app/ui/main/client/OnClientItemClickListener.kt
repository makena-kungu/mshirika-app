package co.ke.mshirika.mshirika_app.ui.main.client

import co.ke.mshirika.mshirika_app.data.response.Client

interface OnClientItemClickListener {
    fun onClickClient(client: Client, position: Int, colors: IntArray)
}