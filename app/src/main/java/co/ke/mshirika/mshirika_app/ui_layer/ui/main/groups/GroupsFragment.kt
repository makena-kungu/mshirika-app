package co.ke.mshirika.mshirika_app.ui_layer.ui.main.groups

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.databinding.FragmentGroupsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State
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
        binding.appBar.toolbarLarge.setupToolbar(R.string.groups, R.menu.search)
        binding.setupRecyclerView()
    }

    private fun FragmentGroupsBinding.setupRecyclerView() {
        val adapter = GroupsAdapter(this@GroupsFragment)
        groups.adapter = adapter
        viewModel.data.collectLatestLifecycle {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = item?.run {
        val searchView = actionView as SearchView

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.state()
            }
        }
        searchView.setOnQueryTextListener(this@GroupsFragment)
        true
    } == true

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