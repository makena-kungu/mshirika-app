package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.databinding.ItemPageIndicatorBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.PageIndicatorAdapter.PageIndicatorViewHolder

class PageIndicatorAdapter(private val size: Int) : ListAdapter<Int, PageIndicatorViewHolder>(diffUtil) {
    override fun submitList(list: MutableList<Int>?) {
        super.submitList((0 until size).toList())
    }

    var selectedPagePosition = 0
        set(value) {
            field = value
            notifyItemRangeChanged(0, itemCount)
        }

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
        holder.bind(position)
    }

    inner class PageIndicatorViewHolder(binding: ItemPageIndicatorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val isSelected = position == selectedPagePosition
            itemView.isSelected = isSelected
            Log.d(TAG, "bind: ($position, $isSelected)")
        }
    }

    companion object {
        private const val TAG = "PageIndicatorAdapter"

        private val diffUtil = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }
        }
    }
}