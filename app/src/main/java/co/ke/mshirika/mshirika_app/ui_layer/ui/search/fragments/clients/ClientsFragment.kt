package co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.clients

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.adapters.ClientsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientsFragment :
    MshirikaFragment<FragmentSearchFragsBinding>(R.layout.fragment_search_frags), OnSearchListener {
    private val viewModel by viewModels<SearchViewModel>()

    private lateinit var listener: OnClientItemClickListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.authKey()
            val adapter = ClientsAdapter(
                lifecycleScope,
                viewModel.authKey,
                listener
            )
            binding.list.adapter = adapter
            viewModel.clients.collectLatestLifecycle {
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
        fun getInstance(searchFragment: SearchFragment): ClientsFragment {
            val fragment = ClientsFragment()
            fragment.listener = searchFragment
            return fragment
        }
    }
}