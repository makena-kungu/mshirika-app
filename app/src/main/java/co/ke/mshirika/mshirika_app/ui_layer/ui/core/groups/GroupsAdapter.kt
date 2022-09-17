package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.databinding.ItemGroupBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.MshirikaPagingDataAdapter

class GroupsAdapter(
    private val listener: OnGroupClickListener
) : MshirikaPagingDataAdapter<Grupo, GroupsAdapter.GroupViewHolder>(Grupo) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder =
        LayoutInflater.from(parent.context)
            .let {
                ItemGroupBinding.inflate(it, parent, false)
            }.let {
                GroupViewHolder(it)
            }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        getItem(position)?.also {
            holder.bind(it)
        }
    }

    inner class GroupViewHolder(private val binding: ItemGroupBinding) :
        ViewHolder(binding.root),
        OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(group: Grupo) {
            binding.group = group
        }

        override fun onClick(v: View?) {
            val pos = absoluteAdapterPosition
            if (pos != NO_POSITION)
                getItem(pos)?.also {
                    listener.onClickGroup(it, pos)
                }
        }
    }
}