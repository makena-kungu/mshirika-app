package co.ke.mshirika.mshirika_app.ui.loans

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.FragmentLoansBinding
import co.ke.mshirika.mshirika_app.ui.DetailsFragment
import co.ke.mshirika.mshirika_app.ui.loans.LoansFragmentDirections.Companion.actionGlobalLoanRepaymentFragment
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.collectLatest

class LoansFragment : DetailsFragment<FragmentLoansBinding>(R.layout.fragment_loans),
    OnLoanClickListener {

    val viewModel by viewModels<LoansViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LoansAdapter(this)
        lifecycleScope.launchWhenCreated {
            viewModel.loans.collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
        binding.apply {
            loans.setHasFixedSize(true)
            loans.adapter = adapter
        }
    }

    override val hasToolbar: Boolean
        get() = true

    override val isTopFragment: Boolean
        get() = true

    override val toolbar: MaterialToolbar
        get() = binding.appBar.toolbarLarge

    override val toolbarTitle: String
        get() = getString(R.string.loans)

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