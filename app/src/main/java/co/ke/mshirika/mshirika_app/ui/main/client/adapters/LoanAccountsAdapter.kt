package co.ke.mshirika.mshirika_app.ui.main.client.adapters

import android.content.res.Resources
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.ItemLoanBinding
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.date
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.drawable

class LoanAccountsAdapter(private val listener: LoanClickListener) :
    ListAdapter<LoanAccount, LoanAccountsAdapter.LoanViewHolder>(LoanAccount) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LoanViewHolder(
            ItemLoanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class LoanViewHolder(private val binding: ItemLoanBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val resources: Resources = itemView.resources

        init {
            binding.loanType.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (absoluteAdapterPosition != NO_POSITION)
                getItem(absoluteAdapterPosition)?.let {
                    listener.loanClicked(it)
                }
        }

        fun bind(acc: LoanAccount) {
            binding.apply {
                loanType.text = acc.productName
                loanDetails.apply {
                    LoanAccountDetailsAdapter(
                        mutableListOf(
                            "Loan Date" to acc.timeline.submittedOnDate.date,
                            "Due Date" to "",//this will be derived from the repayment schedule
                            "Amount Due" to "",
                            "Loan Balance" to ""
                        )
                    ).also {
                        adapter = it
                    }
                }
                val color: IntArray
                when (acc.productName) {
                    "Development Loan" -> {
                        color = colors[2]
                        R.drawable.ic_round_roofing_24
                    }
                    "AgriBusiness Loan" -> {
                        color = colors[2]
                        R.drawable.ic_round_grass_24
                    }
                    "Business Loan" -> {
                        color = colors[1]
                        R.drawable.ic_round_corporate_fare_24
                    }
                    else -> {
                        when {
                            acc.productName.contains(
                                "fees",
                                true
                            ) -> {
                                color = colors[1]
                                R.drawable.ic_round_escalator_warning_24
                            }
                            acc.productName.contains(
                                "medical",
                                true
                            ) -> {
                                color = colors[0]
                                R.drawable.ic_baseline_medical_services_24
                            }
                            else -> {
                                color = colors[3]
                                R.drawable.ic_round_emergency_24
                            }
                        }
                    }
                }
                    .also {
                        ResourcesCompat.getDrawable(resources, it, null)?.let {
                            loanIcon.apply {
                                setImageDrawable(
                                    Size(width, height).drawable(color, it)
                                )
                            }
                        }
                    }
            }
        }

        private val colors =
            arrayOf(
                intArrayOf(
                    color(android.R.color.holo_orange_dark),
                    color(android.R.color.holo_orange_light)
                ),
                intArrayOf(
                    color(android.R.color.holo_blue_dark),
                    color(android.R.color.holo_blue_light)
                ),
                intArrayOf(
                    color(android.R.color.holo_green_dark),
                    color(android.R.color.holo_green_light)
                ),
                intArrayOf(
                    color(android.R.color.holo_red_dark),
                    color(android.R.color.holo_red_light)
                )
            )

        fun color(id: Int) = ResourcesCompat.getColor(resources, id, null)
    }


    interface LoanClickListener {
        fun loanClicked(acc: LoanAccount)
    }
}