package co.ke.mshirika.mshirika_app.ui.adapters

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.ke.mshirika.mshirika_app.databinding.ItemAccountBinding

class AccountsAdapter : ListAdapter<> {

    inner class AccountsViewHolder(binding: ItemAccountBinding) : ViewHolder(binding.root) {

        init {

        }

        fun click() {
            TODO("Open the Clicked Account")
        }
    }
}