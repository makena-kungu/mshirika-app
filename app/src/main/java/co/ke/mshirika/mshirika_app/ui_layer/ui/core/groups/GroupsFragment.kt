package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.databinding.FragmentGroupsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.OnSearchListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupsFragment : MshirikaFragment<FragmentGroupsBinding>(R.layout.fragment_groups),
    SearchView.OnQueryTextListener,
    OnGroupClickListener,
    OnSearchListener {
    private val viewModel by viewModels<GroupsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override val title: String
        get() = getString(R.string.groups)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbarLarge.setupToolbar(
            titleId = R.string.groups,
            resId = R.menu.search
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

    override fun onClickGroup(group: Group, position: Int) {
        // TODO("Not yet implemented")
    }
}