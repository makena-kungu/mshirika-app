package co.ke.mshirika.mshirika_app.ui_layer.ui.main.groups

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
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
import com.google.android.material.appbar.MaterialToolbar
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupRecyclerView()
    }

    private fun FragmentGroupsBinding.setupRecyclerView() {
        val adapter = GroupsAdapter(this@GroupsFragment)
        groups.adapter = adapter
        viewModel.data.collectLatestLifecycle {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.state()
            }
        }
        searchView.setOnQueryTextListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return if (!query.isNullOrBlank()) {
            lifecycleScope.launchWhenCreated {
                viewModel.search(query)
            }
            false
        } else true
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
    override val toolbarTitle: String
        get() = title
    override val toolbar: MaterialToolbar
        get() = binding.appBar.toolbarLarge
    override val isTopFragment: Boolean
        get() = true
    override val menuResId: Int?
        get() = null //R.menu.group
}