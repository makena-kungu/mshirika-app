package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.databinding.ItemPageIndicatorBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.PageIndicatorAdapter.PageIndicatorViewHolder

class PageIndicatorAdapter : ListAdapter<PageIndicator, PageIndicatorViewHolder>(PageIndicator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageIndicatorViewHolder {
        return PageIndicatorViewHolder(
            ItemPageIndicatorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PageIndicatorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PageIndicatorViewHolder(binding: ItemPageIndicatorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(indicator: PageIndicator) {
            val isselected = indicator.isSelected
            val position = absoluteAdapterPosition

            Log.d(TAG, "bind: ($position, $isselected)")
            itemView.isSelected = isselected
        }
    }

    companion object {
        private const val TAG = "PageIndicatorAdapter"
    }
}