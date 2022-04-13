package co.ke.mshirika.mshirika_app.ui.main.client.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.ke.mshirika.mshirika_app.data.response.Account
import co.ke.mshirika.mshirika_app.databinding.ItemAccountBinding

class AccountsAdapter : ListAdapter<Account, AccountsAdapter.AccountsViewHolder>(Account) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AccountsViewHolder(
            ItemAccountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {

    }

    inner class AccountsViewHolder(binding: ItemAccountBinding) : ViewHolder(binding.root) {

        fun click() {

        }
    }
}