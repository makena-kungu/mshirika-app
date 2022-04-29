package co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients

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
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.adapters.ClientsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.viewModels.ClientsViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State.Searching
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailsTransition
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Displays a list of Clients
 */
@AndroidEntryPoint
class ClientsFragment : MshirikaFragment<FragmentClientsBinding>(R.layout.fragment_clients),
    OnQueryTextListener,
    OnClientItemClickListener {

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

        /*viewModel.clients.collectLatestLifecycle {
            adapter.submitData(lifecycle, it)
        }*/
        viewModel.clientes.collectLatestLifecycle {
            adapter.submitData(lifecycle, it)
        }

        adapter.loadStateFlow.collectLatestLifecycle { loadStates ->
            clientsLoading.isVisible = loadStates.refresh is LoadState.Loading
            //show a snackbar
            when (loadStates.refresh) {
                is LoadState.Error -> Snackbar.make(
                    root, "An Error Occurred!",
                    Snackbar.LENGTH_LONG
                ).run {
                    setAction("Retry") {
                        //retry the loading
                        loadStates.refresh
                        dismiss()
                    }
                    show()
                }
                else -> {}
            }
        }
    }

    override fun onClickClient(
        containerView: View,
        client: Client,
        imageUrl: String,
        colors: IntArray
    ) {
        ClientsFragmentDirections.actionGlobalClientFragment(
            client = client,
            clientImageUri = imageUrl,
            colors = colors
        ).also {
            itemToDetailsTransition(
                it
            )
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

    override val toolbar: MaterialToolbar
        get() = binding.appBarLayout.toolbarLarge
    override val toolbarTitle: String
        get() = getString(R.string.clients)
    /*override val menuResId: Int?
        get() = null*/// TODO("Study whether this fragment requires a menu")

    companion object {
        private const val TAG = "ClientsFragment"
    }
}

