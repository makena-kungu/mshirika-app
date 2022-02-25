package co.ke.mshirika.mshirika_app.ui.main.center

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import co.ke.mshirika.mshirika_app.utility.DataStore
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.databinding.FragmentCentersBinding
import co.ke.mshirika.mshirika_app.ui.main.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CentersFragment : Fragment(R.layout.fragment_centers), SearchView.OnQueryTextListener,
    OnCenterClickListener {

    private var _binding: FragmentCentersBinding? = null
    private val binding: FragmentCentersBinding get() = _binding!!
    private val viewModel by viewModels<CentersViewModel>()
    private val lifecycleScope get() = viewLifecycleOwner.lifecycleScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCentersBinding.bind(view)

        lifecycleScope.launchWhenCreated {
            binding.setupRecyclerView()
        }
    }

    private suspend fun FragmentCentersBinding.setupRecyclerView() {
        val adapter = CentersAdapter(this@CentersFragment)
        centers.adapter = adapter
        DataStore(requireContext()).authKey.collectLatest { authKey ->
            viewModel.data(authKey).collectLatest {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return if (!query.isNullOrBlank()) {
            lifecycleScope.launchWhenCreated {
                DataStore(requireContext()).authKey.collectLatest {
                    viewModel.search(query, it)
                }
            }
            false
        } else
            true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            if (it.isNotBlank()) {
                viewModel.state(State.SEARCHING)
            }
        }
        return true
    }

    override fun onClickCenter(center: Center, position: Int) {
        TODO("Not yet implemented")
    }
}