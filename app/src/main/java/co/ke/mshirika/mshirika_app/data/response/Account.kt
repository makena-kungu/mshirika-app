package co.ke.mshirika.mshirika_app.data.response

import androidx.recyclerview.widget.DiffUtil

data class Account(
    val id: Int
) {
    companion object : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
            oldItem == newItem
    }
}
