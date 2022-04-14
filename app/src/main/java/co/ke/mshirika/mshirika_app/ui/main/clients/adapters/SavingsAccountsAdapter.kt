package co.ke.mshirika.mshirika_app.ui.main.clients.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.R.drawable.*
import co.ke.mshirika.mshirika_app.data.response.SavingsAccount
import co.ke.mshirika.mshirika_app.databinding.ItemAccountBinding

class SavingsAccountsAdapter(private val listener: SavingsClickListener) :
    ListAdapter<SavingsAccount, SavingsAccountsAdapter.SavingsViewHolder>(SavingsAccount) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SavingsViewHolder(
            ItemAccountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SavingsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class SavingsViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (absoluteAdapterPosition != NO_POSITION)
                    getItem(absoluteAdapterPosition)?.let {
                        listener.onSavingsClick(it)
                    }
            }
        }

        fun bind(acc: SavingsAccount) {
            binding.details.apply {
                text = acc.productName.let {
                    if (it == "Minor Account") {
                        null //query account details
                    } else it
                }

                when (acc.shortProductName) {
                    "SP" -> ic_round_currency_exchange_24
                    "SLA" -> ic_round_water_drop_24
                    "BF1" -> ic_round_support_24
                    else -> when {
                        acc.productName.contains(
                            "Normal Savings",
                            true
                        ) -> ic_outline_savings_24
                        acc.productName == "Minor Savings" -> ic_round_child_care_24
                        else -> ic_round_attach_money_24
                    }
                }.also {
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        it,
                        0,
                        ic_round_navigate_next_24,
                        0
                    )
                }
            }
        }
    }

    interface SavingsClickListener {
        fun onSavingsClick(acc: SavingsAccount)
    }
}