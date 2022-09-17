package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.DetailedLoanAccount.Transaction
import co.ke.mshirika.mshirika_app.databinding.ItemTransactionLoanBinding

class LoanTransactionsAdapter : ListAdapter<Transaction, LoanTransactionsAdapter.ScheduleViewHolder>(Transaction) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTransactionLoanBinding.inflate(inflater, parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ScheduleViewHolder(
        private val binding: ItemTransactionLoanBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.transaction = transaction
        }
    }
}