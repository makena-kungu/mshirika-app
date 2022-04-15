package co.ke.mshirika.mshirika_app.ui.main.clients

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.databinding.FragmentClientsBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.main.clients.adapters.ClientsAdapter
import co.ke.mshirika.mshirika_app.ui.main.clients.viewModels.ClientsViewModel
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Normal
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Searching
import co.ke.mshirika.mshirika_app.ui.util.Transitions.itemToDetailTransition
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/**
 * Displays a list of Clients
 */
@AndroidEntryPoint
class ClientsFragment : MshirikaFragment<FragmentClientsBinding>(R.layout.fragment_clients),
    OnQueryTextListener,
    OnClientItemClickListener {

    private val viewModel: ClientsViewModel by viewModels()

    private lateinit var searchView: SearchView

    override val hasToolbar: Boolean
        get() = true
    override val isTopFragment: Boolean
        get() = true
    override val toolbar: MaterialToolbar
        get() = binding.appBarLayout.toolbarLarge
    override val toolbarTitle: String
        get() = getString(R.string.clients)
    override val resId: Int
        get() = TODO("Study whether this fragment requires a menu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleScope.launchWhenCreated {
                setupRecyclerView()
            }
        }
    }

    private suspend fun FragmentClientsBinding.setupRecyclerView() {
        val adapter = ClientsAdapter(viewModel.authKey(), this@ClientsFragment)
        clients.adapter = adapter

        viewModel.clients.collectLatest {
            adapter.submitData(lifecycle, it)
        }

        adapter.loadStateFlow.collectLatest { loadStates ->
            //progressBar.isVisible = loadStates.refresh is LoadState.Loading
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
                is LoadState.Loading -> clientsLoading.isVisible = true
                is LoadState.NotLoading -> clientsLoading.isVisible = false
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
            itemToDetailTransition(
                it
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(this)
        searchView.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.state(Normal)
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
}

