package co.ke.mshirika.mshirika_app.ui.search.fragments.clients

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.main.clients.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui.main.clients.adapters.ClientsAdapter
import co.ke.mshirika.mshirika_app.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ClientsFragment :
    MshirikaFragment<FragmentSearchFragsBinding>(R.layout.fragment_search_frags), OnSearchListener {
    private val viewModel by viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            val adapter = ClientsAdapter(
                viewModel.authKey(),
                parentFragment as OnClientItemClickListener
            )
            binding.list.adapter = adapter
            viewModel.clients.collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    override val title: String
        get() = getString(R.string.clients)

    companion object {
        fun getInstance(): ClientsFragment {
            return ClientsFragment()
        }
    }
}