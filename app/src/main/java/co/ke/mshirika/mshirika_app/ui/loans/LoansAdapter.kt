package co.ke.mshirika.mshirika_app.ui.loans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.ItemLoan3Binding
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.amt

class LoansAdapter(
    private val listener: OnLoanClickListener
) : PagingDataAdapter<LoanAccount, LoansAdapter.LoansViewHolder>(LoanAccount) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoansViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLoan3Binding.inflate(inflater, parent, false)
        return LoansViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoansViewHolder, position: Int) {
        getItem(position)?.let {
            holder.binder(it)
        }
    }

    inner class LoansViewHolder(private val binding: ItemLoan3Binding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener
        }

        fun binder(acc:LoanAccount) {
            acc.bind()
        }

        private fun LoanAccount.bind() {
            binding.apply {
                loanProductName.text = loanType.value
                loanClientName.text = clientName
                loanPrincipalAmount.text = principal.amt
                //loanInterestAmount.text =
                //todo extract the due date and amount
            }
        }


    }
}