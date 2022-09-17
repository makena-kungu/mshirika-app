package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.databinding.ItemLoan3Binding
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.ClientFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters.LoanAccountsAdapter.LoanViewHolder
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.date
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mediumDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.amt

class LoanAccountsAdapter(private val fragment: ClientFragment) :
    ListAdapter<ConservativeLoanAccount, LoanViewHolder>(ConservativeLoanAccount) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LoanViewHolder(
        ItemLoan3Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(position, it)
        }
    }

    inner class LoanViewHolder(private val binding: ItemLoan3Binding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position == NO_POSITION) return

            val item = getItem(position) ?: return
            val view = v ?: return
            Log.d(TAG, "onClick: $item")

            when (view.id) {
                binding.makeRepayment.id -> {
                    fragment.onLoanRepayClicked(item, position, binding.root)
                }
                binding.viewLoan.id -> {
                    fragment.onLoanClicked(item, position, binding.root)
                }
                else -> {}
            }
        }

        fun bind(position: Int, loan: ConservativeLoanAccount) {
            binding.makeRepayment.setOnClickListener(this)
            binding.viewLoan.setOnClickListener(this)
            binding.loan = loan
            binding.loanClientImage.isVisible = false
            binding.position = position

            fragment.launch {
                val detailedLoanAccount = fragment.viewModel
                    .getDetailedLoan(loan.id) ?: return@launch

                val today = System.currentTimeMillis()
                val period = detailedLoanAccount.repaymentSchedule.periods.firstOrNull {
                    val from = it.fromDate?.run { date < today } == true
                    val due = it.dueDate.date > today
                    from && due
                } ?: return@launch

                binding.amountDue = period.totalDueForPeriod.amt
                binding.dueDate = period.dueDate.mediumDate
            }
        }

    }

    companion object {
        private const val TAG = "LoanAccountsAdapter"
    }
}