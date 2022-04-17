package co.ke.mshirika.mshirika_app.ui.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.databinding.ItemPageIndicatorBinding
import co.ke.mshirika.mshirika_app.ui.create.PageIndicatorAdapter.PageIndicatorViewHolder

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
            itemView.isSelected = indicator.isSelected
        }
    }
}