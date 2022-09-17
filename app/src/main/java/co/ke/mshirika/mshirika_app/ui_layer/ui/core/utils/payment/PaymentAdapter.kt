package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanFromClientAccounts
import co.ke.mshirika.mshirika_app.databinding.ItemPaymentBinding

class PaymentAdapter : ListAdapter<LoanFromClientAccounts, PaymentAdapter.PaymentViewHolder>(
    LoanFromClientAccounts
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPaymentBinding.inflate(inflater, parent, false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it)
        }
    }

    fun getLoan(position: Int): LoanFromClientAccounts = getItem(position)!!

    inner class PaymentViewHolder(private val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loanAccount: LoanFromClientAccounts) {
            binding.apply {
                loanAccount.apply {
                    bind()
                }
            }
        }

        context (ItemPaymentBinding, LoanFromClientAccounts)
        fun bind() {
            amount.hint = productName

        }
    }
}