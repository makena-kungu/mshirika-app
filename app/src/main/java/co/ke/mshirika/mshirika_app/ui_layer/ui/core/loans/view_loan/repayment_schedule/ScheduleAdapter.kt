package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan.repayment_schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.DetailedLoanAccount.RepaymentSchedule.Period
import co.ke.mshirika.mshirika_app.databinding.ItemRepaymentScheduleBinding

class ScheduleAdapter : ListAdapter<Period, ScheduleAdapter.ScheduleViewHolder>(Period) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepaymentScheduleBinding.inflate(inflater, parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ScheduleViewHolder(
        private val binding: ItemRepaymentScheduleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.context = binding.root.context
        }

        fun bind(period: Period) {
            binding.period = period
        }
    }
}