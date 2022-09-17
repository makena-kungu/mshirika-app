package co.ke.mshirika.mshirika_app.ui_layer.ui.checker_inbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerTask
import co.ke.mshirika.mshirika_app.databinding.ItemCheckerTaskBinding

class CheckerTaskListAdapter : ListAdapter<CheckerTask,
        CheckerTaskListAdapter.CheckerTaskViewHolder>(TaskDiffCallback()) {

    private var mListener: OnItemClickListener? = null
    private var current = NO_POSITION
        set(value) {
            val previous = current
            field = value
            if (previous >= 0) notifyItemChanged(previous)
            if (field == NO_POSITION) return // no such index exists therefore we exit
            notifyItemChanged(current)
        }

    interface OnItemClickListener {
        fun onApproveClick(task: CheckerTask)
        fun onRejectClick(task: CheckerTask)
        fun onDeleteClick(task: CheckerTask)
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<CheckerTask>() {
        override fun areItemsTheSame(oldItem: CheckerTask, newItem: CheckerTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CheckerTask, newItem: CheckerTask): Boolean {
            return oldItem.resourceId == newItem.resourceId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckerTaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCheckerTaskBinding.inflate(inflater, parent, false)
        return CheckerTaskViewHolder(binding)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mListener = onItemClickListener
    }

    override fun onBindViewHolder(holder: CheckerTaskViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, position)
    }

    inner class CheckerTaskViewHolder(private val binding: ItemCheckerTaskBinding) : ViewHolder(
        binding.root
    ), View.OnClickListener {

        fun bind(item: CheckerTask, position: Int) {
            itemView.setOnClickListener(this)

            binding.holder = this
            binding.apply {
                tvCheckerTaskDate.text = item.date
                tvCheckerTaskStatus.text = item.status
                tvCheckerTaskMaker.text = item.maker
                tvCheckerTaskAction.text = root.context.getString(
                    R.string.action_and_entity,
                    item.actionName,
                    item.entityName
                )
            }

            expand(position = position)
        }

        private fun expand(position: Int) {
            binding.flow.isVisible = position == current
        }

        fun approve() {
            mListener?.onApproveClick(getItem(absoluteAdapterPosition))
        }

        fun delete() {
            mListener?.onDeleteClick(getItem(absoluteAdapterPosition))
        }

        fun reject() {
            mListener?.onRejectClick(getItem(absoluteAdapterPosition))
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position == NO_POSITION) return

            if (current != position) {
                current = position
                return
            }

            current = NO_POSITION

            // TODO: invoke a bottom sheet that can show more details about the task
        }
    }

    companion object {
        private const val NO_POSITION = RecyclerView.NO_POSITION
    }
}
