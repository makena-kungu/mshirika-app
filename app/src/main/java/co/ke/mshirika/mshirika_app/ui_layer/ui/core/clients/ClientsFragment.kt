package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.databinding.FragmentClientsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters.ClientsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.viewModels.ClientsViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailsTransition
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import dagger.hilt.android.AndroidEntryPoint

/**
 * Displays a list of Clients
 */
@AndroidEntryPoint
class ClientsFragment : MshirikaFragment<FragmentClientsBinding>(R.layout.fragment_clients),
    OnQueryTextListener, OnClientItemClickListener {

    private val viewModel: ClientsViewModel by navGraphViewModels(R.id.clientsFragment)
    private val toolbar get() = binding.appBarLayout.toolbarLarge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupRecyclerView()
        toolbar.setup(
            titleId = R.string.clients
        )
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item == null || item.itemId != R.id.action_search) return super.onMenuItemClick(item)

        val searchView = item.actionView as SearchView
        Log.d(TAG, "onCreateOptionsMenu: search item not null")
        searchView.setOnQueryTextListener(this)
        searchView.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.normal()
        }
        return true
    }

    private fun FragmentClientsBinding.setupRecyclerView() {
        val adapter = ClientsAdapter(
            scope = lifecycleScope,
            authKey = viewModel.authKey,
            listener = this@ClientsFragment
        )
        clients.adapter = adapter
        clients.setHasFixedSize(false)

        viewModel.clientes.collectLatestLifecycle {
            adapter.submitData(it)
        }
        adapter.loadStateFlow.collectLatestLifecycle { loadStates ->
            val refresh = loadStates.refresh
            val isLoading = refresh is LoadState.Loading
            viewModel.loadingChannel.send(isLoading)
            if (refresh is LoadState.Error)
                viewModel.errorChannel.send(resourceText(R.string.an_error_occurred))

            val isEmpty = refresh is LoadState.NotLoading &&
                    refresh.endOfPaginationReached &&
                    adapter.itemCount == 0
            errorNoData.isVisible = isEmpty
        }
        viewModel.errorState.collectLatestLifecycle {
            root.snackL(it.text(requireContext()))
        }
        viewModel.loadingState.collectLatestLifecycle {
            clientsLoading.isVisible = it
        }
    }

    override fun onClickClient(
        containerView: View,
        client: Client,
        imageUrl: String?,
        colors: IntArray?
    ) {
        ClientsFragmentDirections.actionGlobalClientFragment(
            client = client,
            clientImageUri = imageUrl,
            colors = colors
        ).also {
            itemToDetailsTransition(it)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // do not clear the focus as the clients are searched|filtered
        // if the fragment is in search mode.
        // todo study this behaviour

        query?.let { viewModel.search(it) }
        // searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    companion object {
        private const val TAG = "ClientsFragment"
    }
}

