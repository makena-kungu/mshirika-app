package co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.content

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.databinding.FragmentNewLoanDetailsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.NewLoanViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewLoanDetailsFragment : MshirikaFragment<FragmentNewLoanDetailsBinding>(
    R.layout.fragment_new_loan_details
), ViewerFragment {

    private val viewModel by viewModels<NewLoanViewModel>()
    private val client: Client? get() = viewModel.client

    private val requiredFields = mutableMapOf(
        PRODUCT to "",
        PURPOSE to null,
        DATE to "",
        DIS_DATE to "",
        FORM to "",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateSubtitle(R.string.details)
        val details = viewModel.details
        if (requiredFields.all { (_, value) -> value == null || value.isEmpty() })
            requiredFields += details
        enableButton()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(IO) {
            setupLoanProduct()
        }
        val officeId = client?.officeId ?: return
        binding.loanFormNoLo.prefixText = "0$officeId"
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

    fun product(s: Editable) {
        val key = PRODUCT
        validateNonNull(s, key)
        val product = requiredFields[key] ?: return
        lifecycleScope.launch {
            setupLoanPurpose(product)
        }
    }

    fun purpose(s: Editable) {
        validateNonNull(s, PURPOSE)
    }

    fun date(s: Editable) {
        validateNonNull(s, DATE)
    }

    fun disDate(s: Editable) {
        validateNonNull(s, DIS_DATE)
    }

    fun form(s: Editable) {
        validateNonNull(s, FORM)
    }

    private fun enableButton() {
        // purpose need not have a value so you don't validate that it's not empty
        val disable = requiredFields.filterKeys { key -> key != PURPOSE }.any { (_, value) ->
            value == null || value.isEmpty()
        }
        if (disable) viewModel.disableNext()
        else viewModel.enableNext()
    }

    private fun validateNonNull(s: Editable, key: String) {
        var value = s.toString().trim()
        if (value.isEmpty()) return
        enableButton()
        if (key == FORM) value = "${binding.loanFormNoLo.prefixText}$value"
        requiredFields[key] = value
    }

    override fun onNextPressed() {
        viewModel.cacheDetails(requiredFields)
    }

    companion object {
        const val PRODUCT = "product"
        const val PURPOSE = "purpose"
        const val DATE = "date"
        const val DIS_DATE = "dib_date"
        const val FORM = "form"
    }
}