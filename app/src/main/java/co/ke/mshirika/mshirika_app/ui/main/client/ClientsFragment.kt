package co.ke.mshirika.mshirika_app.ui.main.client

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.databinding.FragmentClientsBinding
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.ClientsAdapter
import co.ke.mshirika.mshirika_app.ui.main.client.viewModels.ClientsViewModel
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Normal
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Searching
import co.ke.mshirika.mshirika_app.ui.util.Transitions.itemToDetailTransition
import co.ke.mshirika.mshirika_app.utility.DataStore
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Displays a list of Clients
 */
@AndroidEntryPoint
class ClientsFragment : Fragment(R.layout.fragment_clients),
    OnQueryTextListener,
    OnClientItemClickListener {

    private var _binding: FragmentClientsBinding? = null
    private var _adapter: ClientsAdapter? = null

    private val binding get() = _binding!!
    private val adapter get() = _adapter!!
    private val scope get() = viewLifecycleOwner.lifecycleScope
    private val _lifecycle get() = viewLifecycleOwner.lifecycle
    private val viewModel: ClientsViewModel by viewModels()
    private val authKey by lazy {
        var authKey: String? = null
        scope.launch { authKey = DataStore(requireContext()).authKey()!! }
        authKey!!
    }

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentClientsBinding.bind(view)
        binding.apply {
            scope.launchWhenCreated {
                setupRecyclerView()
            }
        }
    }

    private fun FragmentClientsBinding.setupRecyclerView() {
        _adapter = ClientsAdapter(authKey, this@ClientsFragment)
        clients.adapter = adapter

        scope.launchWhenCreated {
            viewModel.clients.collectLatest {
                adapter.submitData(_lifecycle, it)
            }

            adapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                if (loadStates.refresh is LoadState.Error) {
                    //show a snackbar
                    Snackbar.make(
                        root, "An Error Occurred!",
                        Snackbar.LENGTH_LONG
                    ).apply {
                        setAction("Retry") {
                            //retry the loading
                            loadStates.refresh
                            dismiss()
                        }
                    }.show()
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

