package co.ke.mshirika.mshirika_app.ui.main.centers

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.databinding.ItemCenter2Binding
import co.ke.mshirika.mshirika_app.ui.main.utils.MyPagingDataAdapter

class CentersAdapter(
    private val listener: OnCenterClickListener
) :
    MyPagingDataAdapter<Center, CentersAdapter.CenterViewHolder>(Center) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCenter2Binding.inflate(inflater, parent, false)
        return CenterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        getItem(position)?.also {
            holder.bind(it)
        }
    }

    inner class CenterViewHolder(private val binding: ItemCenter2Binding) :
        ViewHolder(binding.root),
        OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(center: Center) {
            binding.center = center
        }

        override fun onClick(v: View?) {
            val pos = absoluteAdapterPosition
            if (pos != NO_POSITION)
                getItem(pos)?.also { v?.let { view -> listener.onClickCenter(it, pos, view) } }
        }
    }
}