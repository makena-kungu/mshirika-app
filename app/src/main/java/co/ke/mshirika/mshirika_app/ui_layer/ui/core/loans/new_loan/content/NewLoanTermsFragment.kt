package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewLoanTermsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.NewLoanFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.NewLoanViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import kotlinx.parcelize.Parcelize

class NewLoanTermsFragment : MshirikaFragment<FragmentNewLoanTermsBinding>(
    R.layout.fragment_new_loan_terms
), ViewerFragment {

    private val viewModel by viewModels<NewLoanViewModel>({ requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateSubtitle(R.string.loan_terms)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInputFields()
    }

    private fun setupInputFields() {
        val template = viewModel.template2
        val frequencies = template.repaymentFrequencyTypeOptions.map { it.value }

        binding.loanTerm.setText(template.termFrequency.toString())
        binding.repayEvery.setText(template.repaymentEvery.toString())

        binding.frequency.setAdapter(frequencies)
        binding.repaymentFreq.setAdapter(frequencies)

        binding.apply {
            val parent = requireParentFragment() as NewLoanFragment
            parent.apply {
                binding.goToNext.attachNonVoidFields(
                    loanTerm,
                    repayEvery,
                    repaymentFreq,
                    frequency,
                    principal
                )
            }
        }
    }

    override fun onNextPressed(): Boolean {

        val template = viewModel.template2
        val frequencies = template.repaymentFrequencyTypeOptions
        binding.apply {
            val principal = principal.text().toInt()
            val loanTerm = loanTerm.text().toInt()
            val frequency = frequency.text()
            val repaidEvery = repayEvery.text().toInt()
            val repaymentFrequency = repaymentFreq.text()

            val terms = Terms(
                principal = principal,
                loanTerm = loanTerm,
                frequency = frequencies.first { it.value == frequency }.id,
                repaidEvery = repaidEvery,
                repaymentFrequency = repaymentFrequency
            )
            viewModel.cacheTerms(terms)
        }
        return true
    }

    companion object {

        @Parcelize
        data class Terms(
            val principal: Int,
            val loanTerm: Int,
            val frequency: Int,
            val repaidEvery: Int,
            val repaymentFrequency: String
        ) : Parcelable
    }
}