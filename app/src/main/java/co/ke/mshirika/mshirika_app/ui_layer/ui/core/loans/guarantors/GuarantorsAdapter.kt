package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.guarantors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanWithGuarantors
import co.ke.mshirika.mshirika_app.databinding.ItemLoanGuarantorBinding

class GuarantorsAdapter : ListAdapter<LoanWithGuarantors.Guarantor, GuarantorsAdapter.GuarantorViewHolder>(
    LoanWithGuarantors.Guarantor
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuarantorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLoanGuarantorBinding.inflate(inflater, parent, false)
        return GuarantorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuarantorViewHolder, position: Int) {
        val guarantor = getItem(position) ?: return
        holder.bind(guarantor)
    }

    inner class GuarantorViewHolder(private val binding: ItemLoanGuarantorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(guarantor: LoanWithGuarantors.Guarantor) {
            binding.guarantor = guarantor
        }
    }
}