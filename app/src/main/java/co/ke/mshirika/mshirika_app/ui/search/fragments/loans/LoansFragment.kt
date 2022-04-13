package co.ke.mshirika.mshirika_app.ui.search.fragments.loans

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui.loans.LoansAdapter
import co.ke.mshirika.mshirika_app.ui.loans.OnLoanClickListener
import co.ke.mshirika.mshirika_app.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui.search.SearchViewModel
import kotlinx.coroutines.flow.collectLatest

class LoansFragment : Fragment(R.layout.fragment_search_frags), OnSearchListener {
    private var _binding: FragmentSearchFragsBinding? = null
    private var listener: OnLoanClickListener? = null

    private val binding get() = _binding!!
    private val lifecycleScope get() = viewLifecycleOwner.lifecycleScope
    private val vm by viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchFragsBinding.bind(view)
        val adapter = LoansAdapter(listener!!)
        binding.list.adapter = adapter
        lifecycleScope.launchWhenCreated {
            vm.loans.collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val title: String
        get() = getString(R.string.loans)

    companion object {
        fun getInstance(listener: OnLoanClickListener): LoansFragment {
            val fragment = LoansFragment()
            fragment.listener = listener
            return fragment
        }
    }
}