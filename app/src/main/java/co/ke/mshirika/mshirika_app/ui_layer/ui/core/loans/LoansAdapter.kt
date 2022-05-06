package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.ItemLoan3Binding
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.date
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mediumDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.amt

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
            holder.binder(it, position)
        }
    }

    inner class LoansViewHolder(private val binding: ItemLoan3Binding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.apply {
                root.setOnClickListener(this@LoansViewHolder)
                clientMakeRepayment.setOnClickListener {
                    val position = absoluteAdapterPosition
                    if (position == NO_POSITION)
                        return@setOnClickListener

                    getItem(position)?.let { account ->
                        listener.onLoanRepayClicked(
                            account,
                            position,
                            root
                        )
                    }
                }
            }
        }

        override fun onClick(v: View?) {
            listener
        }

        fun binder(acc: LoanAccount, position: Int) {
            acc.apply {
                binding.apply {
                    bind(position)
                }
            }
        }

        context (LoanAccount, ItemLoan3Binding)
                private fun LoanAccount.bind(pos: Int) {
            binding.apply {
                position = pos
                loanProductName.text = loanType.value
                loanClientName.text = clientName
                loanPrincipalAmount.text = principal.amt
                //loanInterestAmount.text = // TODO:
                repaymentSchedule.periods.first {
                    val time = System.currentTimeMillis()
                    it.fromDate.date < time && it.dueDate.date > time
                }.also {
                    dueDate = it.dueDate.date.mediumDate
                    amountDue = it.totalDueForPeriod.amt
                }
            }
        }


    }
}