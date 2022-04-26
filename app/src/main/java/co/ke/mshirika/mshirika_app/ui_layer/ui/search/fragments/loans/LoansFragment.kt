package co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.loans

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.loans.LoansAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.loans.OnLoanClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchViewModel
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import kotlinx.coroutines.flow.collectLatest

class LoansFragment : MshirikaFragment<FragmentSearchFragsBinding>(R.layout.fragment_search_frags), OnSearchListener {
    private val viewModel by viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LoansAdapter(parentFragment as OnLoanClickListener)
        binding.list.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.loans.collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    override val title: String
        get() = getString(R.string.loans)

    companion object {
        fun getInstance(): LoansFragment {
            return LoansFragment()
        }
    }
}