package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content

import androidx.recyclerview.widget.DiffUtil

data class Charge(
    val name: String,
    val amount: String
) {
    companion object : DiffUtil.ItemCallback<Charge>() {
        override fun areItemsTheSame(oldItem: Charge, newItem: Charge): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Charge, newItem: Charge): Boolean {
            return oldItem == newItem
        }
    }
}
