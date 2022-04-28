package co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Loan
import co.ke.mshirika.mshirika_app.data_layer.repositories.Other
import co.ke.mshirika.mshirika_app.databinding.FragmentPaymentBinding
import co.ke.mshirika.mshirika_app.databinding.ItemPaymentBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : MshirikaFragment<FragmentPaymentBinding>(R.layout.fragment_payment) {

    private val args by navArgs<PaymentFragmentArgs>()
    private val accounts by lazy {
        args.accounts
    }
    private val client by lazy {
        args.client
    }

    private val viewModel: PaymentViewModel by viewModels()

    lateinit var adapter: PaymentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.client(client)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupViews()
    }

    private fun FragmentPaymentBinding.setupViews() {
        adapter = PaymentAdapter()
        adapter.submitList(accounts.loans)
        loanAccs.adapter = adapter

        viewModel.template.collectLatestLifeCycleNonNull { template ->
            val list = template.paymentTypes.map {
                it.name
            }

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                list
            )
            paymentMode.setAdapter(adapter)
        }
    }

    fun submit() {
        binding.submit()
    }

    fun FragmentPaymentBinding.submit() {
        val map = mutableMapOf<Loan, Pair<Double, Double>>()
        (0..adapter.itemCount).forEach { position ->
            loanAccs.layoutManager!!.findViewByPosition(position)?.let {
                with(ItemPaymentBinding.bind(it)) {
                    val text = amount.text()
                    val amt = if (text.isNotBlank()) text.toDouble() else null
                    val chargeTxt = accountCharge.text()
                    val charge = if (chargeTxt.isNotBlank()) chargeTxt.toDouble() else .0
                    amt?.let { amount ->
                        val loan = adapter.getLoan(position)
                        map += loan to (amount to charge)
                    }
                }
            }
        }

        val amountText = sharesAccount.amount.text()
        val amount = if (amountText.isNotBlank()) amountText.toDouble() else null
        val chargeText = sharesAccount.accountCharge.text()
        val charge = if (chargeText.isNotBlank()) chargeText.toDouble() else .0

        val shares = amount?.let {
            Triple(accounts.savingsAccounts.first(), it, charge)
        }
        val other = Other(
            transactionDate = transactionDate.text().fromShortDate,
            modeId = -1,
            receiptNo = receiptNo.text(),
            bankDate = bankDate.text().fromShortDate
        )

        viewModel.pay(paymentMode.text(), shares, map, other)
    }
}