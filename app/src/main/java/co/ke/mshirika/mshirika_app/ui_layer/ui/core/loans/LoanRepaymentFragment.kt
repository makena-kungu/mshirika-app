package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentLoanRepaymentBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromLongDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.s
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.OperationalUtils.openDatePicker
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.UIText
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar

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
        handleError()
        handleLoading()
        handleSuccess()
        binding.loadAccount()
    }

    private fun handleError() {
        viewModel.errorState.collectLatestLifecycle { uiText ->
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

    private fun handleLoading() {
        viewModel.loadingState.collectLatestLifecycle {
            binding.transactionsLoading.isVisible = it
        }
    }

    private fun handleSuccess() {
        viewModel.successState.collectLatestLifecycle {
            Snackbar.make(binding.root, it.text(requireContext()), LENGTH_LONG).show()
        }
    }

    private fun FragmentLoanRepaymentBinding.openRepaymentCalendar() {
        repaymentDateL.setEndIconOnClickListener {
            openDatePicker(R.string.repayment_date) { date ->
                repaymentDate.setText(date.shortDate)
            }
        }
    }

    private fun FragmentLoanRepaymentBinding.openBankCalendar() {
        repaymentBankDateL.setEndIconOnClickListener {
            openDatePicker(R.string.bank_date) { date ->
                repaymentBankDate.setText(date.shortDate)
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

    private fun FragmentLoanRepaymentBinding.loadAccount() {
        val adapter = LoanTransactionsAdapter()
        loanTransactions.adapter = adapter
        viewModel.loanAccount.collectLatestLifeCycleNonNull {
            loanAccount = it
            adapter.submitList(it.transactions)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }
}