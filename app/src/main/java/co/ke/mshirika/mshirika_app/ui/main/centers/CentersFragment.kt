package co.ke.mshirika.mshirika_app.ui.main.centers

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.databinding.FragmentCentersBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui.util.Transitions.itemToDetailReentry
import co.ke.mshirika.mshirika_app.ui.util.Transitions.itemToDetailTransition
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CentersFragment : MshirikaFragment<FragmentCentersBinding>(R.layout.fragment_centers),
    SearchView.OnQueryTextListener,
    OnCenterClickListener,
    OnSearchListener {

    private val viewModel by viewModels<CentersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemToDetailReentry(view)

        lifecycleScope.launchWhenCreated {
            binding.setupRecyclerView()
        }
    }

    override val toolbar: MaterialToolbar
        get() = binding.appBar.toolbarLarge

    override val hasToolbar: Boolean
        get() = true

    override val isTopFragment: Boolean
        get() = true

    override val toolbarTitle: String
        get() = getString(R.string.centers)

    private suspend fun FragmentCentersBinding.setupRecyclerView() {
        val adapter = CentersAdapter(this@CentersFragment)
        centers.adapter = adapter
        viewModel.data.collectLatest {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return if (!query.isNullOrBlank()) {
            lifecycleScope.launchWhenCreated {
                viewModel.search(query)
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

    override fun onClickCenter(center: Center, position: Int, view: View) {
        CentersFragmentDirections.actionGlobalCenterFragment().also {
            itemToDetailTransition(it, view to getString(R.string.center_card_transition_name))
        }
    }

    override val title: String
        get() = getString(R.string.centers)
}