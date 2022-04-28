package co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.groups

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.groups.GroupsAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.groups.OnGroupClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GroupsFragment : MshirikaFragment<FragmentSearchFragsBinding>(R.layout.fragment_search_frags),
    OnSearchListener {

    private lateinit var listener: OnGroupClickListener
    private val viewModel by viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = GroupsAdapter(listener)
        binding.list.adapter = adapter
        lifecycleScope.launchWhenCreated {
            viewModel.groups.collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    override val title: String
        get() = getString(R.string.groups)

    companion object {
        fun getInstance(searchFragment: SearchFragment): GroupsFragment {
            val fragment = GroupsFragment()
            fragment.listener = searchFragment
            return fragment
        }
    }
}