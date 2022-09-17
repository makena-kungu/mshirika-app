package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.GetsRepaymentSchedule
import co.ke.mshirika.mshirika_app.databinding.ItemLoan3Binding
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.date
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mediumDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.now
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.amt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoansAdapter(
    private val scheduleGetter: GetsRepaymentSchedule,
    private val listener: OnLoanClickListener,
    private val coroutineScope: CoroutineScope
) : PagingDataAdapter<ConservativeLoanAccount, LoansAdapter.LoansViewHolder>(ConservativeLoanAccount) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoansViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLoan3Binding.inflate(inflater, parent, false)
        return LoansViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoansViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, position)
        }
    }

    inner class LoansViewHolder(private val binding: ItemLoan3Binding) : RecyclerView.ViewHolder(
        binding.root
    ), View.OnClickListener {

        private val root get() = binding.root

        override fun onClick(v: View?) {
            if (v == null) return

            val pos = absoluteAdapterPosition
            if (pos == NO_POSITION) return
            val account = getItem(pos) ?: return
            when (v.id) {
                binding.makeRepayment.id -> {
                    listener.onLoanRepayClicked(
                        account,
                        pos,
                        root
                    )
                }
                binding.viewLoan.id -> {
                    listener.onLoanClicked(account, pos, root)
                }
            }
        }

        fun bind(loan: ConservativeLoanAccount, position: Int) {
            Log.d(TAG, "bind: invoked")
            root.setOnClickListener(this)
            binding.makeRepayment.setOnClickListener(this)
            binding.viewLoan.setOnClickListener(this)
            binding.loan = loan
            binding.position = position

            coroutineScope.launch {
                val result = scheduleGetter.repaymentSchedule(loan.id) ?: return@launch
                result.repaymentSchedule?.periods?.firstOrNull { period ->
                    val time = System.currentTimeMillis()
                    Log.d(TAG, "bind: period = $period")
                    val from = period.fromDate?.run { date < time } == true
                    val to = period.dueDate?.run { date > time } == true
                    from and to
                }?.also {
                    binding.dueDate = it.dueDate?.date?.mediumDate
                    binding.amountDue = it.totalDueForPeriod.amt
                } ?: kotlin.run {
                    //check for the largest period
                    result.repaymentSchedule?.periods?.takeUnless { list ->
                        list.any { it.dueDate.isNullOrEmpty() }
                    }?.maxOfOrNull {
                        it.dueDate!!.date
                    }?.also {
                        if (it < now) binding.dueDate = "Overdue"
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "LoansAdapter"
    }
}