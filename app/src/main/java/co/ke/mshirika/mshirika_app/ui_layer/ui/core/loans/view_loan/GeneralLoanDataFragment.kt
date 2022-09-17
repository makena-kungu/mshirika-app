package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentViewLoanGeneralPageBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralLoanDataFragment : MshirikaFragment<FragmentViewLoanGeneralPageBinding>(
    R.layout.fragment_view_loan_general_page
) {
    private val viewModel by viewModels<LoanViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.prestamo.observe(viewLifecycleOwner) {
            it?.let { loan ->
                binding.loan = loan
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item == null) return false

        return when (item.itemId) {
            R.id.loanRepaymentScheduleFragment -> {
                val directions = GeneralLoanDataFragmentDirections
                    .actionGeneralLoanDataFragmentToLoanRepaymentScheduleFragment()
                findNavController().navigate(directions)
                true
            }
            R.id.loanTransactionsFragment -> {
                val directions = GeneralLoanDataFragmentDirections
                    .actionGeneralLoanDataFragmentToLoanTransactionsFragment()
                findNavController().navigate(directions)
                true
            }
            else -> false
        }
    }
}