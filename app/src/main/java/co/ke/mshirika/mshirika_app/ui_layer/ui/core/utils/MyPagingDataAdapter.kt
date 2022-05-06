package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class MyPagingDataAdapter<T : Any, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, VH>(diffCallback) {

    val colorMapping = mutableMapOf<Int, IntArray>()
}