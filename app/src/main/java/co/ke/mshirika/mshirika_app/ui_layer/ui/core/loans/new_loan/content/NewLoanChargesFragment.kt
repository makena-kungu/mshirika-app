package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.databinding.FragmentNewLoanChargesBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.NewLoanViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.clear
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.amt
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter

class NewLoanChargesFragment : MshirikaFragment<FragmentNewLoanChargesBinding>(
    R.layout.fragment_new_loan_charges
), ViewerFragment {

    private val viewModel by viewModels<NewLoanViewModel>({requireParentFragment()})
    private val charges get() = viewModel.template2.chargeOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateSubtitle(R.string.charges)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val charges = charges.map { it.name }
        binding.charge.setAdapter(charges)

        val adapter = ChargesAdapter()
        binding.charges.adapter = adapter
        viewModel.chargesFlow.collectLatestLifecycle {
            val template = viewModel.template2
            val chargeList = it.map { charge ->
                val chargeOption = template
                    .chargeOptions
                    .first { option -> option.id == charge.chargeId }
                Charge(
                    name = chargeOption.name,
                    amount = charge.amount.toDouble().amt
                )
            }

            adapter.submitList(chargeList)
        }
    }

    fun addCharge() {
        val text = binding.charge.text()
        if (text.isEmpty()) {
            binding.charge.error = getString(R.string.required)
            return
        }
        charges.map { it.chargeCalculationType.value }
        val charge = charges.find { it.name == text } ?: return
        val type = charge.chargeCalculationType.value
        val amount = if (type == "% Amount") {
            // find the principal and do the math
            val principal = viewModel.terms?.principal ?: return
            principal * charge
        } else charge.amount

        val c = NewLoan.Charge(charge.id, amount.toInt())
        viewModel.cacheCharges(c)
        binding.charge.clear()
        //make an adapter
    }

    override fun onNextPressed() = true

    private operator fun Int.times(charge: NewLoanTemplate2.ChargeOption): Double {
        val percentage = charge.amount * .01
        return this * percentage
    }
}

