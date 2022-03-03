package co.ke.mshirika.mshirika_app.ui.main.client.adapters

import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.ItemLoanBinding
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.LoanAccountsAdapter.LoanViewHolder
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.amt
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.date
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.drawable
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.mediumDate
import java.util.*
import kotlin.math.abs

class LoanAccountsAdapter(private val listener: LoanClickListener) :
    ListAdapter<LoanAccount, LoanViewHolder>(LoanAccount) {

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
            holder.bindLoan(it)
        }
    }

    inner class LoanViewHolder(private val binding: ItemLoanBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val resources
            get() = itemView.context.resources
        private val theme
            get() = itemView.context.theme

        init {
            binding.clientMakeRepayment.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (absoluteAdapterPosition != NO_POSITION)
                getItem(absoluteAdapterPosition)?.let {
                    listener.onClickLoan(it)
                }
        }

        fun bindLoan(loan: LoanAccount) {
            loan.bind()
        }

        fun LoanAccount.bind() {
            binding.apply {
                loanType.text = loanProductName
                loanDetails.adapter = LoanAccountDetailsAdapter(list)
                val color: IntArray

                when (loanProductName) {
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
                            loanProductName.contains(
                                "fees",
                                true
                            ) -> {
                                color = colors[1]
                                R.drawable.ic_round_escalator_warning_24
                            }
                            loanProductName.contains(
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
                }.also {
                    ResourcesCompat.getDrawable(resources, it, theme)?.let {
                        loanIcon.apply {
                            setImageDrawable(
                                Size(width, height).drawable(color, it)
                            )
                        }
                    }
                }

            }
        }

        private val LoanAccount.list: MutableList<Pair<String, String>>
            get() {
                val list =
                    mutableListOf(getString(R.string.loan_date) to timeline.submittedOnDate.mediumDate)
                val period = repaymentSchedule.periods.find {
                    //finding the best fitting date
                    val time = Calendar.getInstance().timeInMillis
                    time >= it.fromDate.date && time <= it.dueDate.date
                }

                period?.apply {
                    list += getString(R.string.due_date) to dueDate.mediumDate
                    val amountDue = interestDue + principalDue
                    val loanBal =
                        abs((principalPaid + interestPaid) - principalDisbursed)
                    list += getString(R.string.amount_due) to amountDue.amt
                    list += getString(R.string.loan_balance) to loanBal.amt
                }

                return list
            }

        private fun getString(@StringRes resId: Int): String {
            return resources.getString(resId)
        }

        val colors =
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
        fun onClickLoan(acc: LoanAccount)
    }
}