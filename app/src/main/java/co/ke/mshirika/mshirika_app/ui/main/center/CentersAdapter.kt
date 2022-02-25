package co.ke.mshirika.mshirika_app.ui.main.center

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.databinding.ItemCenterBinding
import co.ke.mshirika.mshirika_app.ui.main.utils.MyPagingDataAdapter
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.drawable
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.random

class CentersAdapter(
    private val listener: OnCenterClickListener
) :
    MyPagingDataAdapter<Center, CentersAdapter.CenterViewHolder>(Center) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder =
        LayoutInflater.from(parent.context)
            .let {
                ItemCenterBinding.inflate(it, parent, false)
            }.let {
                CenterViewHolder(it)
            }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        getItem(position)?.also {
            holder.bind(it)
        }
    }

    inner class CenterViewHolder(private val binding: ItemCenterBinding) :
        ViewHolder(binding.root),
        OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(center: Center) {
            binding.bind(center)
        }

        private fun ItemCenterBinding.bind(center: Center) {
            center.apply {
                centerImage.apply {
                    text = name[0].uppercase()
                    context.random.let {
                        colorMapping[absoluteAdapterPosition] = it
                        drawable(it)
                    }
                }

                centerName.text = name
                root.context.apply {
                    centerAccountNo.text = getString(R.string.account_no, accountNo)
                    centerExternalId.text = getString(R.string.external_id, externalId)
                }
            }
        }

        override fun onClick(v: View?) {
            val pos = absoluteAdapterPosition
            if (pos != NO_POSITION)
                getItem(pos)?.also {
                    listener.onClickCenter(it, pos)
                }
        }
    }
}