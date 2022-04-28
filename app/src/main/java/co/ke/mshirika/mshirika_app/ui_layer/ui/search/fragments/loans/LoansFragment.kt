package co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.loans

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.loans.LoansAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.loans.OnLoanClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoansFragment : MshirikaFragment<FragmentSearchFragsBinding>(R.layout.fragment_search_frags),
    OnSearchListener {
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var listener: OnLoanClickListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LoansAdapter(listener)
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
        fun getInstance(searchFragment: SearchFragment): LoansFragment {
            val fragment = LoansFragment()
            fragment.listener = searchFragment
            return fragment
        }
    }
}