package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.databinding.ItemLoanAccDetailsDenseBinding

class LoanAccountDetailsAdapter(private val list: MutableList<Pair<String, String>>) :
    RecyclerView.Adapter<LoanAccountDetailsAdapter.DetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DetailsViewHolder(
        ItemLoanAccDetailsDenseBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
    )

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun update(newList: List<Pair<String, String>>) {
        val size = itemCount
        list.clear()
        list.addAll(newList)
        notifyItemRangeChanged(0, size)
    }

    class DetailsViewHolder(private val binding: ItemLoanAccDetailsDenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pair: Pair<String, String>) {
            binding.pair = pair
        }
    }
}