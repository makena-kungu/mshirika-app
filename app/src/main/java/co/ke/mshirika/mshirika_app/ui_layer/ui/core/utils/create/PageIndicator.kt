package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create

import androidx.recyclerview.widget.DiffUtil

data class PageIndicator(
    val index: Int,
    val isSelected: Boolean = false
) {
    companion object : DiffUtil.ItemCallback<PageIndicator>() {
        override fun areItemsTheSame(oldItem: PageIndicator, newItem: PageIndicator): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(oldItem: PageIndicator, newItem: PageIndicator): Boolean {
            return oldItem == newItem
        }
    }
}
