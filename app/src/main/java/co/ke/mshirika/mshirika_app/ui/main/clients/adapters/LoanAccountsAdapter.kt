package co.ke.mshirika.mshirika_app.ui.main.clients.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.ItemLoan3Binding
import co.ke.mshirika.mshirika_app.ui.main.clients.adapters.LoanAccountsAdapter.LoanViewHolder

class LoanAccountsAdapter(private val listener: co.ke.mshirika.mshirika_app.ui.loans.OnLoanClickListener) :
    ListAdapter<LoanAccount, LoanViewHolder>(LoanAccount) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LoanViewHolder(
            ItemLoan3Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindLoan(it)
        }
    }

    inner class LoanViewHolder(private val binding: ItemLoan3Binding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.clientMakeRepayment.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (absoluteAdapterPosition != NO_POSITION)
                getItem(absoluteAdapterPosition)?.let {
                    listener.onLoanClicked(it)
                }
        }

        fun bindLoan(loan: LoanAccount) {
            binding.loan = loan
            binding.loanClientImage.isVisible = false
        }

    }


}