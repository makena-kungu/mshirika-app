package co.ke.mshirika.mshirika_app.ui.loans

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.FragmentLoansBinding
import co.ke.mshirika.mshirika_app.ui.util.DetailsFragment
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

    override val toolbar: Toolbar
        get() = binding.appBar.toolbarLarge

    override val toolbarTitle: String
        get() = getString(R.string.loans)

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onLoanClicked(loanAccount: LoanAccount) {
        TODO("Not yet implemented")
    }
}