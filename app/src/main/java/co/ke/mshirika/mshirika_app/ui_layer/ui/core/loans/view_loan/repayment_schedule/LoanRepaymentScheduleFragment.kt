package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan.repayment_schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentRepaymentScheduleBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan.LoanViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan.observeNonNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanRepaymentScheduleFragment : MshirikaFragment<FragmentRepaymentScheduleBinding>(
    R.layout.fragment_repayment_schedule
) {
    private val viewModel by viewModels<LoanViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ScheduleAdapter()
        binding.scheduleList.adapter = adapter
        viewModel.prestamo.observeNonNull(viewLifecycleOwner) {
            adapter.submitList(it.repaymentSchedule.periods)
        }
    }
}