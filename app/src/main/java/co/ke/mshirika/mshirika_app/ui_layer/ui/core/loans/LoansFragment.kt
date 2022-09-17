package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.filter
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.databinding.FragmentLoansBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.DetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoansFragment : DetailsFragment<FragmentLoansBinding>(
    R.layout.fragment_loans
), OnLoanClickListener {

    private val viewModel by viewModels<LoansViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LoansAdapter(
            scheduleGetter = viewModel,
            listener = this,
            coroutineScope = lifecycleScope
        )
        binding.apply {
            appBar.toolbarLarge.setup(R.string.loans)
            loans.adapter = adapter
            viewModel.loans.observe(viewLifecycleOwner) {
                adapter.submitData(fragmentLifecycle, it)
            }
            viewModel.prestamos.observe(viewLifecycleOwner) {
                Log.d(TAG, "onViewCreated: ${it.size}")
                Log.d(TAG, "onViewCreated: ${it.joinToString()}")
            }
        }

        adapter.loadStateFlow.collectLatestLifecycle {
            val refresh = it.refresh
            binding.loansLoading.isVisible = refresh is LoadState.Loading
        }
    }

    override fun onLoanClicked(
        loanAccount: ConservativeLoanAccount,
        position: Int,
        container: View
    ) {
        val dirs = LoansFragmentDirections.actionFragmentLoansToViewLoanFragment2(
            loanAccount.id,
            loanAccount.clientId,
            loanAccount.clientName
        )

        findNavController().navigate(dirs)
    }

    override fun onLoanRepayClicked(
        loanAccount: ConservativeLoanAccount,
        position: Int,
        container: View
    ): Boolean {
        Log.d(TAG, "onLoanRepayClicked: loanAccount $loanAccount")
        val dirs = LoansFragmentDirections.actionFragmentLoansToLoanRepaymentFragment(
            clientName = loanAccount.clientName,
            clientId = loanAccount.clientId,
            loanId = loanAccount.id
        )
        findNavController().navigate(dirs)
        return true
    }

    companion object {
        private const val TAG = "LoansFragment"
    }
}