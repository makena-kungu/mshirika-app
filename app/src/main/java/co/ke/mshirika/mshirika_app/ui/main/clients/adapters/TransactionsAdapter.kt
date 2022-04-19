package co.ke.mshirika.mshirika_app.ui.main.clients.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Transaction
import co.ke.mshirika.mshirika_app.databinding.ItemTransactionBinding
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.mediumDate
import co.ke.mshirika.mshirika_app.ui.util.ViewUtils.amt

class TransactionsAdapter(private val listener: OnTransactionsItemClickListener) :
    ListAdapter<Transaction, TransactionsAdapter.TransactionsViewHolder>(Transaction) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder =
        LayoutInflater.from(parent.context).let { inflater ->
            ItemTransactionBinding.inflate(
                inflater,
                parent,
                false
            ).let {
                TransactionsViewHolder(it)
            }
        }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class TransactionsViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            absoluteAdapterPosition.takeIf {
                it != NO_POSITION
            }?.also {
                listener.onClickTransaction(transaction = getItem(it))
            }
        }

        fun bind(transaction: Transaction) {
            binding.bind(transaction)
        }

        private fun ItemTransactionBinding.bind(transaction: Transaction) {
            with(transaction) {
                transactionAmount.text = amount.amt
                transactionDate.text = submittedOnDate.mediumDate
                transactionId.text = id.toString()
                transactionName.text = transactionType.value
                when (transactionType.value) {
                    "Deposit" -> {
                        R.drawable.ic_deposit
                    }
                    else -> null
                }?.also {
                    transactionImage.setImageResource(it)
                }
            }
        }
    }

    interface OnTransactionsItemClickListener {
        fun onClickTransaction(transaction: Transaction)
    }
}