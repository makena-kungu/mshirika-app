package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.databinding.FragmentClientsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.adapters.ClientsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.viewModels.ClientsViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State.Searching
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailsTransition
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import dagger.hilt.android.AndroidEntryPoint

/**
 * Displays a list of Clients
 */
@AndroidEntryPoint
class ClientsFragment : MshirikaFragment<FragmentClientsBinding>(R.layout.fragment_clients),
    OnQueryTextListener, OnClientItemClickListener {

    private val viewModel: ClientsViewModel by viewModels()

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupRecyclerView()
        binding.appBarLayout.toolbarLarge.setupToolbar(
            R.string.clients,
            R.menu.search
        )
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
            Log.d(TAG, "setupRecyclerView: is loading = $isLoading")
            viewModel.loadingChannel.send(isLoading)
            //show a snackbar
            when (refresh) {
                is LoadState.Error -> root.snackL("An Error Occurred!") {
                    setAction("Retry") {
                        //retry the loading
                        loadStates.refresh
                        dismiss()
                    }
                }
                else -> {}
            }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        searchItem?.run {
            actionView as SearchView
        }?.also {
            Log.d(TAG, "onCreateOptionsMenu: search item not null")
            searchView = it
            searchView.setOnQueryTextListener(this)
            searchView.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) viewModel.state()
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { viewModel.search(it) }
        searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            //first start the searching scope
            if (it.isNotBlank()) viewModel.state(Searching)
        }
        return true
    }

    companion object {
        private const val TAG = "ClientsFragment"
    }
}

