package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.databinding.ItemLoan3Binding
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters.LoanAccountsAdapter.LoanViewHolder
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.OnLoanClickListener

class LoanAccountsAdapter(private val listener: OnLoanClickListener) :
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
            holder.bindLoan(it)
        }
    }

    inner class LoanViewHolder(private val binding: ItemLoan3Binding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position == NO_POSITION) return

            val item = getItem(position) ?: return
            Log.d(TAG, "onClick: $item")
            listener.onLoanRepayClicked(item, position, binding.root)
        }

        fun bindLoan(loan: ConservativeLoanAccount) {
            binding.clientMakeRepayment.setOnClickListener(this)
            binding.loan = loan
            binding.loanClientImage.isVisible = false
        }

    }

    companion object {
        private const val TAG = "LoanAccountsAdapter"
    }
}