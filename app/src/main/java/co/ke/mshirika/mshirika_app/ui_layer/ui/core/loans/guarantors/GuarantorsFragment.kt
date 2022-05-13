package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.guarantors

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.LoanWithGuarantors
import co.ke.mshirika.mshirika_app.databinding.FragmentGuarantorsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.guarantors.GuarantorsViewModel.BottomSheetState.*
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuarantorsFragment : MshirikaFragment<FragmentGuarantorsBinding>(
    R.layout.fragment_guarantors
) {

    private val args by navArgs<GuarantorsFragmentArgs>()
    private val guarantor = mutableMapOf<String, String>()
    private val viewModel by viewModels<GuarantorsViewModel>()
    private val clientName: String get() = args.clientName
    private val clientId: Int get() = args.clientId
    private val loanId: Int get() = args.loanId
    private val guarantors: List<LoanWithGuarantors.Guarantor> get() = args.guarantors.toList()

    private val bottomSheet by lazy {
        from(binding.bottomSheet).apply {
            state = STATE_HIDDEN
            binding.bottomSheet.isVisible = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.guarantors(guarantors)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbarLarge.setup(clientName)
        viewModel.bottomSheetState.observe(viewLifecycleOwner) {
            val (scrim, state) = when (it) {
                Expanded -> true to STATE_EXPANDED
                Hidden -> false to STATE_HIDDEN
                Collapsed -> false to STATE_COLLAPSED
            }
            binding.scrim.isVisible = scrim
            bottomSheet.state = state
        }

        val adapter = GuarantorsAdapter()
        binding.guarantors.adapter = adapter
        viewModel.guarantors.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.clients.observe(viewLifecycleOwner) { list ->
            val clients = list.map { it.displayName }
            binding.client.setAdapter(clients)
        }
        binding.client.setOnItemClickListener { _, _, position, _ ->
            val list = viewModel.clients.value ?: return@setOnItemClickListener
            val client = list[position]
            viewModel.selectedClient(client, loanId)
        }
    }

    fun hide() {
        viewModel.hide()
    }

    fun expand() {
        viewModel.expand()
    }

    fun name(s: Editable) {
        val key = NAME
        validate(s, key)
        viewModel.searchClient(guarantor[key]!!)
    }

    fun relationship(s: Editable) {
        validate(s, RELATIONSHIP)
    }

    fun amount(s: Editable) {
        validate(s, AMOUNT)
        val amount = guarantor[AMOUNT]
        //check whether the amount is available
    }

    private fun validate(s: Editable, key: String) {
        if (s.trim().isEmpty()) return
        val text = s.toString()
        guarantor[key] = text
        //update the state of the button
        val gs = viewModel.guarantors.value ?: return
        if (gs.isEmpty()) return
        binding.submit.isEnabled = guarantor.size < 2
    }

    fun submit() {
        val template = viewModel.template

        viewModel.addGuarantor(
            name = guarantor[NAME]!!,
            amount = guarantor[AMOUNT]!!.toInt(),
            guarantorTypeId = template.guarantorType.id,
            clientId = clientId,
            loanId = loanId
        )
    }

    companion object {
        const val NAME = "name"
        const val RELATIONSHIP = "relationship"
        const val AMOUNT = "amount"
    }
}