package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.GetsRepaymentSchedule
import co.ke.mshirika.mshirika_app.databinding.ItemLoan3Binding
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.date
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mediumDate
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

        init {
            binding.apply {

            }
        }

        override fun onClick(v: View?) {
            if (v == null) return

            val pos = absoluteAdapterPosition
            if (pos == NO_POSITION) return
            val account = getItem(pos) ?: return
            when (v.id) {
                binding.clientMakeRepayment.id -> {
                    Log.d(TAG, "onClick: $account")
                    listener.onLoanRepayClicked(
                        account,
                        pos,
                        root
                    )
                }
                else -> {
                    listener.onLoanClicked(account, pos, root)
                    // TODO("create a new button and do binding...")
                }
            }
        }

        fun bind(loan: ConservativeLoanAccount, position: Int) {
            root.setOnClickListener(this)
            binding.clientMakeRepayment.setOnClickListener(this)
            binding.loan = loan
            binding.position = position

            coroutineScope.launch {
                val result = scheduleGetter.repaymentSchedule(loan.id) ?: return@launch
                result.repaymentSchedule.periods?.first {
                    val time = System.currentTimeMillis()
                    it.fromDate.date < time && it.dueDate.date > time
                }?.also {
                    binding.dueDate = it.dueDate.date.mediumDate
                    binding.amountDue = it.totalDueForPeriod.amt
                }
            }
        }
    }

    companion object {
        private const val TAG = "LoansAdapter"
    }
}