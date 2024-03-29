package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.databinding.FragmentGroupsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupsFragment : MshirikaFragment<FragmentGroupsBinding>(R.layout.fragment_groups),
    SearchView.OnQueryTextListener,
    OnGroupClickListener {
    private val viewModel by activityViewModels<GroupsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbarLarge.setup(
            titleId = R.string.groups
        )
        binding.setupRecyclerView()
    }

    private fun FragmentGroupsBinding.setupRecyclerView() {
        val adapter = GroupsAdapter(this@GroupsFragment)
        groups.adapter = adapter
        viewModel.data.collectLatestLifecycle {
            adapter.submitData(it)
        }
        adapter.loadStateFlow.collectLatestLifecycle {
            val refresh = it.refresh
            groupsLoading.isVisible = refresh is LoadState.Loading
            val isEmpty = refresh is LoadState.NotLoading &&
                    refresh.endOfPaginationReached &&
                    adapter.itemCount == 0
            errorNoData.isVisible = isEmpty
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item == null) return false

        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.state()
            }
        }
        searchView.setup()
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean = if (!query.isNullOrBlank()) {
        lifecycleScope.launchWhenCreated {
            viewModel.search(query)
        }
        true
    } else false

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            if (it.isNotBlank()) {
                viewModel.state(State.Searching)
            }
        }
        return true
    }

    override fun onClickGroup(group: Grupo, position: Int) {
        val dirs = GroupsFragmentDirections.actionGroupsFragmentToGroupFragment(group)
        findNavController().navigate(dirs)
    }
}