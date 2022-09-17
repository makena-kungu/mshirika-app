package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentViewLoanBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanFragment : MshirikaFragment<FragmentViewLoanBinding>(R.layout.fragment_view_loan) {

    private val args by navArgs<LoanFragmentArgs>()
    private val loanId by lazy { args.loanId }
    private val clientId by lazy { args.clientId }
    private val clientName by lazy { args.clientName }
    private val navController by lazy { findNavController() }
    private val viewModel by viewModels<LoanViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loanId = loanId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            title = clientName
            val appBarConfig = AppBarConfiguration(
                navGraph = navController.graph,
                fallbackOnNavigateUpListener = {
                    navController.navigateUp()
                    true
                }
            )
            setupWithNavController(navController, appBarConfig)
        }
    }

    fun makeRepayment() {
        val directions = LoanFragmentDirections.actionGlobalLoanRepaymentFragment(
            clientName = clientName,
            clientId = clientId,
            loanId = loanId
        )
        navController.navigate(directions)
    }
}