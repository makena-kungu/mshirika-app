package co.ke.mshirika.mshirika_app.ui.main.group

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.databinding.FragmentGroupsBinding
import co.ke.mshirika.mshirika_app.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.utility.DataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GroupsFragment : Fragment(R.layout.fragment_groups), SearchView.OnQueryTextListener,
    OnGroupClickListener,
    OnSearchListener {

    private var _binding: FragmentGroupsBinding? = null
    private val binding: FragmentGroupsBinding get() = _binding!!
    private val viewModel by viewModels<GroupsViewModel>()
    private val lifecycleScope get() = viewLifecycleOwner.lifecycleScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGroupsBinding.bind(view)

        lifecycleScope.launchWhenCreated {
            binding.setupRecyclerView()
        }
    }

    private suspend fun FragmentGroupsBinding.setupRecyclerView() {
        val adapter = GroupsAdapter(this@GroupsFragment)
        groups.adapter = adapter
        DataStore(requireContext()).authKey.collectLatest { authKey ->
            viewModel.data().collectLatest {
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
                    viewModel.search(query)
                }
            }
            false
        } else
            true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            if (it.isNotBlank()) {
                viewModel.state(State.Searching)
            }
        }
        return true
    }

    override fun onClickGroup(group: Group, position: Int) {
        TODO("Not yet implemented")
    }

    override val title: String
        get() = getString(R.string.groups)
}