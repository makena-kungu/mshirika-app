package co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Loan
import co.ke.mshirika.mshirika_app.databinding.ItemPaymentBinding

class PaymentAdapter : ListAdapter<Loan, PaymentAdapter.PaymentViewHolder>(Loan) {

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

    override fun getItem(position: Int): Loan {
        return super.getItem(position)
    }

    fun getLoan(position: Int) = getItem(position)

    inner class PaymentViewHolder(private val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loanAccount: Loan) {
            binding.apply {
                loanAccount.apply {
                    bind()
                }
            }
        }

        context (ItemPaymentBinding, Loan)
        fun bind() {
            amount.hint = productName

        }
    }
}