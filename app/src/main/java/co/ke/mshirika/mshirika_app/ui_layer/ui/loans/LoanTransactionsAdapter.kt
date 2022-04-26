package co.ke.mshirika.mshirika_app.ui_layer.ui.loans

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount.LoanTransaction
import co.ke.mshirika.mshirika_app.databinding.ItemTransactionLoanBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.loans.LoanTransactionsAdapter.LoanTransactionsViewHolder

class LoanTransactionsAdapter :
    ListAdapter<LoanTransaction, LoanTransactionsViewHolder>(LoanTransaction) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanTransactionsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTransactionLoanBinding.inflate(layoutInflater, parent, false)
        return LoanTransactionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoanTransactionsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class LoanTransactionsViewHolder(private val binding: ItemTransactionLoanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: LoanTransaction) {
            binding.transaction = transaction
        }
    }
}