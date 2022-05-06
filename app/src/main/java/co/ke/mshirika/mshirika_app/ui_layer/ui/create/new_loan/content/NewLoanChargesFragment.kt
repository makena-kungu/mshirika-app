package co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.content

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.NewLoan
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.databinding.FragmentNewLoanChargesBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.NewLoanViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.content.NewLoanTermsFragment.Companion.PRINCIPAL
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter

class NewLoanChargesFragment : MshirikaFragment<FragmentNewLoanChargesBinding>(
    R.layout.fragment_new_loan_charges
), ViewerFragment {

    private val viewModel by viewModels<NewLoanViewModel>()
    private val list = mutableListOf<String>()
    private val charges get() = viewModel.template2.chargeOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateSubtitle(R.string.charges)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val charges = charges.map { it.name }
        binding.charge.setAdapter(charges)
        viewModel.chargesFlow.collectLatestLifecycle {
            // todo setup
        }
    }

    fun addCharge() {
        val text = binding.charge.text()
        charges.map { it.chargeCalculationType.value }
        val charge = charges.find { it.name == text } ?: return
        val type = charge.chargeCalculationType.value
        val amount = if (type == "% Amount") {
            // find the principal and do the math
            val principal = viewModel.terms[PRINCIPAL] ?: return
            principal.toInt() * charge
        } else charge.amount

        val c = NewLoan.Charge(charge.id, amount.toInt())
        viewModel.cacheCharges(c)
        //make an adapter
    }

    override fun onNextPressed() {

    }

    private operator fun Int.times(charge: NewLoanTemplate2.ChargeOption): Double {
        val percentage = charge.amount * .01
        return this * percentage
    }
}

