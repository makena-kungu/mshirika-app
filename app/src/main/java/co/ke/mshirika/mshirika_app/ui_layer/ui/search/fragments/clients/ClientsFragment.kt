package co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.clients

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters.ClientsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientsFragment : MshirikaFragment<FragmentSearchFragsBinding>(
    R.layout.fragment_search_frags
), OnSearchListener {
    private val viewModel by viewModels<SearchViewModel>()

    private lateinit var listener: OnClientItemClickListener
    private lateinit var adapter: ClientsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ClientsAdapter(
            lifecycleScope,
            viewModel.authKey,
            listener
        )
        binding.list.adapter = adapter
        binding.errorNoData.setText(R.string.no_clients_found)
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    override fun search(query: String) {
        viewModel.load()
        viewModel.clientes(query).observe(viewLifecycleOwner) {
            viewModel.stopLoading()
            adapter.submitData(fragmentLifecycle, it)
            val count = adapter.itemCount
            Log.d(TAG, "search: count = $count")
            binding.errorNoData.isVisible = count == 0
        }
    }

    override val title: String
        get() = getString(R.string.clients)

    companion object {
        private const val TAG = "ClientsFragment"

        fun getInstance(searchFragment: SearchFragment): ClientsFragment {
            val fragment = ClientsFragment()
            fragment.listener = searchFragment
            return fragment
        }
    }
}