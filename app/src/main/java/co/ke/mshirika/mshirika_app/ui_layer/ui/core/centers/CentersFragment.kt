package co.ke.mshirika.mshirika_app.ui_layer.ui.core.centers

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Center
import co.ke.mshirika.mshirika_app.databinding.FragmentCentersBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.ListFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailsTransition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CentersFragment : ListFragment<FragmentCentersBinding>(R.layout.fragment_centers),
    SearchView.OnQueryTextListener,
    OnCenterClickListener,
    OnSearchListener {

    private val viewModel by navGraphViewModels<CentersViewModel>(R.id.centersFragment)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbarLarge.setup(R.string.centers)
        binding.setupRecyclerView()
    }

    private fun FragmentCentersBinding.setupRecyclerView() {
        val adapter = CentersAdapter(this@CentersFragment)
        centers.adapter = adapter
        viewModel.data.collectLatestLifecycle {
            adapter.submitData(it)
        }

        adapter.loadStateFlow.collectLatestLifecycle {
            val refresh = it.refresh
            centersLoading.isVisible = refresh is LoadState.Loading
            val isEmpty = refresh is LoadState.NotLoading &&
                    refresh.endOfPaginationReached &&
                    adapter.itemCount == 0
            errorNoData.isVisible = isEmpty
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(this)
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.state()
        }
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

    override fun onClickCenter(center: Center, position: Int, view: View) {
        CentersFragmentDirections.actionGlobalCenterFragment().also {
            itemToDetailsTransition(it, view to getString(R.string.center_card_transition_name))
        }
    }

    override val title: String
        get() = getString(R.string.centers)

    override fun search(query: String) {

    }
}