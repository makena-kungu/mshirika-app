package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.response.RepaymentResponse
import co.ke.mshirika.mshirika_app.databinding.FragmentLoanRepaymentBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.fromShortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.attachNonVoidFields
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.clear
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.s
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.OperationalUtils.openDatePicker
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.UIText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.setAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoanRepaymentFragment : MshirikaFragment<FragmentLoanRepaymentBinding>(
    R.layout.fragment_loan_repayment
) {

    private val args by navArgs<LoanRepaymentFragmentArgs>()
    private val toolbar get() = binding.appBar.toolbarLarge
    private val viewModel by viewModels<LoansViewModel>({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setup(R.string.loan_repayment)
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

        lifecycleScope.launch {
            val response: RepaymentResponse = viewModel.repaymentTypes() ?: return@launch
            val types = response.asIterable().map { it.name }
            paymentType.setAdapter(types)
        }
        makeRepayment.setOnClickListener {
            val amount = repaymentAmount.s
            val type = paymentType.s
            val code = receipt.s
            val repaymentDate = repaymentDate.s
            val bankDate = repaymentBankDate.s

            val array = arrayOf(
                amount,
                type,
                code,
                repaymentDate,
                bankDate
            )
            Log.d(TAG, "setupOnRepayment: ${array.joinToString()} ")

            launch {
                val result = viewModel.repay(
                    args.loanId,
                    amount,
                    type,
                    code,
                    repaymentDate.fromShortDate,
                    bankDate.fromShortDate
                )

                val message = if (result == null) R.string.error_making_repayment
                else {
                    clear(
                        repaymentAmount,
                        paymentType,
                        receipt,
                        this@setupOnRepayment.repaymentDate,
                        repaymentBankDate,
                    )
                    R.string.repayment_successful
                }

                binding.root.snackL(message)
            }
        }
    }

    private fun FragmentLoanRepaymentBinding.loadAccount() {
        val adapter = LoanTransactionsAdapter()
        loanTransactions.adapter = adapter
        lifecycleScope.launch {
            transactionsLoading.isVisible = true
            val account = viewModel.detailedLoanAccount(args.loanId)
            transactionsLoading.isVisible = false
            if (account == null) return@launch
            val transactions = account.transactions
            adapter.submitList(transactions)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "LoanRepaymentFragment"
    }
}