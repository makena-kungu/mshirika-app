package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.FragmentLoansBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.DetailsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.LoansFragmentDirections.Companion.actionGlobalLoanRepaymentFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoansFragment : DetailsFragment<FragmentLoansBinding>(R.layout.fragment_loans),
    OnLoanClickListener {

    val viewModel by viewModels<LoansViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LoansAdapter(this)
        viewModel.loans.collectLatestLifecycle {
            adapter.submitData(it)
        }
        binding.apply {
            appBar.toolbarLarge.setupToolbar(R.string.loans)
            loans.setHasFixedSize(true)
            loans.adapter = adapter
        }
    }

    override fun onLoanClicked(loanAccount: LoanAccount) {

    }

    override fun onLoanRepayClicked(
        loanAccount: LoanAccount,
        position: Int,
        container: View
    ): Boolean {
        viewModel.setLoanAccount(loanAccount)
        actionGlobalLoanRepaymentFragment().also {
            findNavController().navigate(it)
        }
        return true
    }
}