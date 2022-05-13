package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.paging.filter
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.databinding.FragmentLoansBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.DetailsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.LoansFragmentDirections.Companion.actionGlobalLoanRepaymentFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoansFragment : DetailsFragment<FragmentLoansBinding>(
    R.layout.fragment_loans
), OnLoanClickListener {

    val viewModel by navGraphViewModels<LoansViewModel>(R.id.fragmentLoans)

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
                val list = it.filter { account -> account.status.active }
                adapter.submitData(fragmentLifecycle, list)
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

    }

    override fun onLoanRepayClicked(
        loanAccount: ConservativeLoanAccount,
        position: Int,
        container: View
    ): Boolean {
        Log.d(TAG, "onLoanRepayClicked: loanAccount $loanAccount")
        val dirs = actionGlobalLoanRepaymentFragment(
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