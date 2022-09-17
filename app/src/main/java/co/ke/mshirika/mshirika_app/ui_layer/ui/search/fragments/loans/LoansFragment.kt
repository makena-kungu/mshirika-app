package co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.loans

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.LoansAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.OnLoanClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoansFragment : MshirikaFragment<FragmentSearchFragsBinding>(R.layout.fragment_search_frags),
    OnSearchListener {

    private val viewModel by viewModels<SearchViewModel> (ownerProducer = {
        requireParentFragment()
    })
    private val listener: OnLoanClickListener get() = requireParentFragment() as SearchFragment

    private lateinit var adapter: LoansAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LoansAdapter(
            viewModel,
            listener,
            lifecycleScope
        )
        binding.list.adapter = adapter
        binding.errorNoData.setText(R.string.no_loans_found)

        viewModel.prestamos.observe(viewLifecycleOwner) {
            adapter.submitData(fragmentLifecycle, it)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    override val title: String
        get() = getString(R.string.loans)
}