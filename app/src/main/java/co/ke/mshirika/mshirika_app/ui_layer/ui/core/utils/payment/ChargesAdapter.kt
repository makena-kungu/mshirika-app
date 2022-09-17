package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.DetailedSavingsAccount.Charge
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.amt

class ChargesAdapter : ListAdapter<Charge, ChargesAdapter.ChargesViewHolder>(Charge) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
        return ChargesViewHolder(root)
    }

    override fun onBindViewHolder(holder: ChargesViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it)
        }
    }

    inner class ChargesViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {

        fun bind(charge: Charge) {
            val text1 = root.findViewById<TextView>(android.R.id.text1)
            val text2 = root.findViewById<TextView>(android.R.id.text2)

            text1.text = charge.name
            text2.text = charge.amount.amt
        }
    }
}