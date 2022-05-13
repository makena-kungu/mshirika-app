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
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class NewLoanDetailsFragment : MshirikaFragment<FragmentNewLoanDetailsBinding>(
    R.layout.fragment_new_loan_details
), ViewerFragment {

    private val viewModel by viewModels<NewLoanViewModel>({requireParentFragment()})
    private val client get() = viewModel.client

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
        enableButton()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(IO) { setupLoanProduct() }
        client?.officeId?.let {
            binding.loanFormNoLo.prefixText = "0$it"
        }

        val parentFragment = requireParentFragment() as NewLoanFragment
        binding.apply{
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
        val key = PRODUCT
        validateNonNull(s, key)
        val product = requiredFields[key] ?: return
        launch { setupLoanPurpose(product) }
        launch { setupStaff(product) }
    }

    fun officer(s: Editable) {
        validateNonNull(s, OFFICER)
    }

    fun purpose(s: Editable) {
        validateNonNull(s, PURPOSE)
    }

    fun date(s: Editable) {
        validateNonNull(s, DATE)
    }

    fun disbursementDate(s: Editable) {
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
        value = when (key) {
            FORM -> "${binding.loanFormNoLo.prefixText}$value"
            DATE, DIS_DATE -> value.fromShortDate.mshirikaDate
            else -> value
        }
        requiredFields[key] = value
    }

    override fun onNextPressed(): Boolean {
        val purposeNamed = requiredFields[PURPOSE]!!
        val officer = requiredFields[OFFICER]
        val template = viewModel.template2

        requiredFields[PRODUCT] = template.loanProductId.toString()
        requiredFields[OFFICER] =
            template.loanOfficerOptions.first { it.displayName == officer }.id.toString()
        requiredFields[PURPOSE] = template.loanPurposeOptions
            .first { it.name == purposeNamed }
            .id.toString()
        viewModel.cacheDetails(Details(requiredFields))
        clear(
            binding.product,
            binding.officer,
            binding.purpose,
            binding.loanFormNo,
            binding.date,
            binding.disbursementDate
        )
        return true
    }

    companion object {
        const val PRODUCT = "product"
        const val OFFICER = "officer"
        const val PURPOSE = "purpose"
        const val DATE = "date"
        const val DIS_DATE = "dib_date"
        const val FORM = "form"

        @Parcelize
        data class Details(
            val product: Int,
            val officer: Int,
            val purpose: Int,
            val date: String,
            val disbursementDate: String,
            val formNumber: String // alias external id
        ) : Parcelable {
            constructor(map: Map<String, String?>) : this(
                product = map[PRODUCT]!!.toInt(),
                officer = map[OFFICER]!!.toInt(),
                purpose = map[PURPOSE]!!.toInt(),
                date = map[DATE]!!,
                disbursementDate = map[DIS_DATE]!!,
                formNumber = map[FORM]!!
            )
        }
    }
}