package co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.content

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewLoanTermsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.NewLoanViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter

class NewLoanTermsFragment : MshirikaFragment<FragmentNewLoanTermsBinding>(
    R.layout.fragment_new_loan_terms
), ViewerFragment {

    private val viewModel by viewModels<NewLoanViewModel>()
    private val requiredFields = mutableMapOf(
        PRINCIPAL to "",
        LOAN_TERM to null,
        FREQUENCY to "",
        REPAID_EVERY to "",
        REPAYMENT_FREQUENCY to "",
    )

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
    }

    fun principal(s: Editable) {
        validateNonNull(s, PRINCIPAL)
    }

    fun loanTerm(s: Editable) {
        validateNonNull(s, LOAN_TERM)
    }

    fun frequency(s: Editable) {
        validateNonNull(s, FREQUENCY)
    }

    fun repaidEvery(s: Editable) {
        validateNonNull(s, REPAID_EVERY)
    }

    fun repaymentFrequency(s: Editable) {
        validateNonNull(s, REPAYMENT_FREQUENCY)
    }

    private fun enableButton() {
        val disabled = requiredFields.any { (_, value) -> value == null || value.isEmpty() }
        if (disabled) viewModel.disableNext()
        else viewModel.enableNext
    }

    private fun validateNonNull(s: Editable, key: String) {
        val value = s.toString().trim()
        if (value.isEmpty()) return
        enableButton()
        requiredFields[key] = value
    }

    override fun onNextPressed() {
        viewModel.cacheTerms(requiredFields)
    }

    companion object {
        const val PRINCIPAL = "principal"
        const val LOAN_TERM = "loanTerm"
        const val FREQUENCY = "frequency"
        const val REPAID_EVERY = "repaidEvery"
        const val REPAYMENT_FREQUENCY = "repaymentFrequency"
    }
}