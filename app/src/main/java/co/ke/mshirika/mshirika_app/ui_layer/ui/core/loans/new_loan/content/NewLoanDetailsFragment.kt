package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewLoanDetailsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.NewLoanFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.NewLoanViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.clear
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class NewLoanDetailsFragment : MshirikaFragment<FragmentNewLoanDetailsBinding>(
    R.layout.fragment_new_loan_details
), ViewerFragment {

    private val viewModel by viewModels<NewLoanViewModel>({ requireParentFragment() })
    private val client get() = viewModel.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateSubtitle(R.string.details)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(IO) { setupLoanProduct() }
        client?.officeId?.let {
            binding.loanFormNoLo.prefixText = "0$it"
        }

        val parentFragment = requireParentFragment() as NewLoanFragment
        binding.apply {
            parentFragment.binding.goToNext.attachNonVoidFields(
                product,
                purpose,
                date,
                disbursementDate,
                loanFormNo
            )
        }
    }

    private suspend fun setupLoanProduct() {
        val client = client ?: return
        val template = viewModel.getLoanTemplate(client) ?: return

        val products = template.productOptions.map { it.name }
        binding.product.setAdapter(products)
    }

    private suspend fun setupLoanPurpose(productName: String) {
        val template = viewModel.getLoanTemplate(productName) ?: return
        val purposes = template.loanPurposeOptions.map { it.name }
        binding.purpose.setAdapter(purposes)
    }

    private suspend fun setupStaff(productName: String) {
        val template = viewModel.getLoanTemplate(productName) ?: return
        val officers = template.loanOfficerOptions.map { it.displayName }
        binding.officer.setAdapter(officers)
    }

    fun product(s: Editable) {
        validateNonNull(s)
        val product = s.toString()
        launch { setupLoanPurpose(product) }
        launch { setupStaff(product) }
    }

    private fun validateNonNull(s: Editable): Boolean {
        val value = s.toString().trim()
        if (value.isEmpty()) return false
        return true
    }

    override fun onNextPressed(): Boolean {
        binding.apply {
            val template = viewModel.template2
            val details = Details(
                product = template.loanProductId,
                officer = template.loanOfficerOptions
                    .firstOrNull { it.displayName == officer.text() }?.id
                    ?: return false,
                purpose = template.loanPurposeOptions
                    .firstOrNull { it.name == purpose.text() }?.id
                    ?: return false,
                formNumber = "${binding.loanFormNoLo.prefixText}${loanFormNo.text()}",
                date = date.text().fromShortDate.mshirikaDate,
                disbursementDate = disbursementDate.text().fromShortDate.mshirikaDate
            )

            viewModel.cacheDetails(details)

            clear(product, officer, purpose, loanFormNo, date, disbursementDate)
        }
        return true
    }

    companion object {

        @Parcelize
        data class Details(
            val product: Int,
            val officer: Int,
            val purpose: Int,
            val date: String,
            val disbursementDate: String,
            val formNumber: String // alias external id
        ) : Parcelable
    }
}