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
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.s
import co.ke.mshirika.mshirika_app.ui.util.OperationalUtils.openDatePicker
import co.ke.mshirika.mshirika_app.ui.util.UIText
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
                else -> {
                    uiText.text(requireContext()).also {
                        Snackbar.make(binding.root, it, LENGTH_LONG).show()
                    }
                }
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
            Snackbar.make(binding.root, it.text(requireContext()), LENGTH_LONG).show()
        }
    }

    private fun FragmentLoanRepaymentBinding.openRepaymentCalendar() {
        repaymentDateL.setEndIconOnClickListener {
            openDatePicker("Repayment Date") {
                repaymentDate.setText(it.shortDate)
            }
        }
    }

    private fun FragmentLoanRepaymentBinding.openBankCalendar() {
        repaymentBankDateL.setEndIconOnClickListener {
            openDatePicker("Bank Date") {
                repaymentBankDate.setText(it.shortDate)
            }
        }
    }

    private fun FragmentLoanRepaymentBinding.setupOnRepayment() {
        makeRepayment.attachNonVoidFields(
            repaymentAmount,
            paymentType,
            receipt,
            repaymentDate,
            repaymentBankDate
        )

        makeRepayment.setOnClickListener {
            /*val c = requireContext()
            var canProceed = false

            if (amount == null || type == null || code == null || repaymentDate == null || bankDate == null)
                return@setOnClickListener

            if (canProceed) {*/

            val amount = repaymentAmount.s
            val type = paymentType.s
            val code = receipt.s
            val repaymentDate = repaymentDate.s
            val bankDate = repaymentBankDate.s

            viewModel.repay(
                amount,
                type,
                code,
                repaymentDate.fromLongDate,
                bankDate.fromLongDate
            )
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