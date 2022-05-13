package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChargesAdapter : ListAdapter<Charge, ChargesAdapter.ChargesViewHolder>(Charge) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            android.R.layout.simple_list_item_2,
            parent,
            false
        )
        return ChargesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChargesViewHolder, position: Int) {
        val charge = getItem(position) ?: return
        holder.bind(charge)
    }

    inner class ChargesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //simple list item 2
        private val name = itemView.findViewById<TextView>(android.R.id.text1)
        private val amount = itemView.findViewById<TextView>(android.R.id.text2)

        fun bind(charge: Charge) {
            name.text = charge.name
            amount.text = charge.amount
        }
    }
}