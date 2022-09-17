package co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.groups

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups.GroupsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups.OnGroupClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupsFragment : MshirikaFragment<FragmentSearchFragsBinding>(R.layout.fragment_search_frags),
    OnSearchListener {

    private val listener: OnGroupClickListener
        get() = requireParentFragment() as SearchFragment
    private val viewModel by viewModels<SearchViewModel>({ requireParentFragment() })

    private lateinit var adapter: GroupsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GroupsAdapter(listener)
        binding.list.adapter = adapter
        binding.errorNoData.setText(R.string.no_groups_found)

        viewModel.grupos.observe(viewLifecycleOwner) {
            adapter.submitData(fragmentLifecycle, it)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    override val title: String
        get() = getString(R.string.groups)

    companion object {
        private const val TAG = "GroupsFragment"
    }
}