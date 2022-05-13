package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.databinding.ItemGroupBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.MshirikaPagingDataAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.drawable
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.randomColors

class GroupsAdapter(
    private val listener: OnGroupClickListener
) :
    MshirikaPagingDataAdapter<Group, GroupsAdapter.GroupViewHolder>(Group) {

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

        fun bind(group: Group) {
            binding.bind(group)
        }

        private fun ItemGroupBinding.bind(group: Group) {
            group.apply {
                groupImage.apply {
                    text = name[0].uppercase()
                    context.randomColors.let {
                        colorMapping[absoluteAdapterPosition] = it
                        drawable(it)
                    }
                }

                groupName.text = name
            }
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