package co.ke.mshirika.mshirika_app.ui.loans

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentLoanRepaymentBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.fromLongDate
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.longDate
import co.ke.mshirika.mshirika_app.ui.util.UIText
import co.ke.mshirika.mshirika_app.ui.util.Utils.openDatePicker
import co.ke.mshirika.mshirika_app.ui.util.ViewUtils.nonEmptyText
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class LoanRepaymentFragment :
    MshirikaFragment<FragmentLoanRepaymentBinding>(R.layout.fragment_loan_repayment) {

    private val viewModel by viewModels<LoansViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            appBar.toolbarLarge.title = getString(R.string.loan_repayment)
            openRepaymentCalendar()
            openBankCalendar()
            setupOnRepayment()
        }
        lifecycleScope.launchWhenCreated {
            handleError()
            handleLoading()
            handleSuccess()
            binding.loadAccount()
        }
    }

    private suspend fun handleError() {
        viewModel.errorState.collectLatest { uiText ->
            when (uiText) {
                is UIText.DynamicText -> Snackbar
                    .make(binding.root, uiText.text, LENGTH_LONG).apply {
                        uiText.title?.let {
                            setAction(it) {
                                uiText.action()
                            }
                        }
                        show()
                    }
                is UIText.ResourceText -> uiText.text(requireContext())
            }
        }
    }

    private suspend fun handleLoading() {
        viewModel.loadingState.collectLatest {
            binding.transactionsLoading.isVisible = it
        }
    }

    private suspend fun handleSuccess() {
        viewModel.successState.collectLatest {
            Snackbar.make(binding.root, it, LENGTH_LONG).show()
        }
    }

    private fun FragmentLoanRepaymentBinding.openRepaymentCalendar() {
        repaymentDateL.setEndIconOnClickListener {
            openDatePicker("Repayment Date", parentFragmentManager) {
                repaymentDate.setText(it.longDate)
            }
        }
    }

    private fun FragmentLoanRepaymentBinding.openBankCalendar() {
        repaymentBankDateL.setEndIconOnClickListener {
            openDatePicker("Bank Date", parentFragmentManager) {
                repaymentBankDate.setText(it.longDate)
            }
        }
    }

    private fun FragmentLoanRepaymentBinding.setupOnRepayment() {
        makeRepayment.setOnClickListener {
            val c = requireContext()
            var canProceed = false
            val amount = repaymentAmount.nonEmptyText("Amount", c) { canProceed = it == true }
            val type = paymentType.nonEmptyText("Payment type", c) { canProceed = it == true }
            val code = receipt.nonEmptyText("Receipt Code", c) { canProceed = it == true }
            val repaymentDate = repaymentDate.nonEmptyText("", c) { canProceed = it == true }
            val bankDate = repaymentBankDate.nonEmptyText("", c) { canProceed = it == true }

            if (canProceed) {
                viewModel.repay(
                    amount!!,
                    type!!,
                    code!!,
                    repaymentDate!!.fromLongDate,
                    bankDate!!.fromLongDate
                )
            }
        }
    }

    private suspend fun FragmentLoanRepaymentBinding.loadAccount() {
        val adapter = LoanTransactionsAdapter()
        loanTransactions.adapter = adapter
        viewModel.loanAccount.collectLatest {
            it?.run {
                loanAccount = this
                adapter.submitList(transactions)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }
}