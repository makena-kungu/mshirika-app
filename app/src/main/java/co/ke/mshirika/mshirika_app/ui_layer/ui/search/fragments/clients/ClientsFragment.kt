package co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.clients

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertHeaderItem
import androidx.paging.map
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
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
    private val viewModel by viewModels<SearchViewModel>({ requireParentFragment() })
    private val listener: OnClientItemClickListener get() = requireParentFragment() as SearchFragment

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

        viewModel.clientes.observe(viewLifecycleOwner) {
            adapter.submitData(fragmentLifecycle, it)
        }
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    override val title: String
        get() = getString(R.string.clients)

    companion object {
        private const val TAG = "ClientsFragment"

    }
}