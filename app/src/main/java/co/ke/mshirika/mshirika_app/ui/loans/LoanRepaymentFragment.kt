package co.ke.mshirika.mshirika_app.ui.loans

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.FragmentLoanRepaymentBinding
import co.ke.mshirika.mshirika_app.ui.util.OnFragmentsAttach

class LoanRepaymentFragment : Fragment(R.layout.fragment_loan_repayment),OnLoanClickListener {

    private var _bind: FragmentLoanRepaymentBinding? = null
    private val bind get() = _bind!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bind = FragmentLoanRepaymentBinding.bind(view)

        bind.apply {

        }
    }

    override fun onLoanClicked(loanAccount: LoanAccount) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        with(context as OnFragmentsAttach) {
            hideAppBar()
        }
    }
}