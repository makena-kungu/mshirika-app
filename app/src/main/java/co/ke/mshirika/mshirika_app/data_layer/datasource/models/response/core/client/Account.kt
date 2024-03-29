package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client

import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil

@Keep
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
