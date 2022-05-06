package co.ke.mshirika.mshirika_app.ui_layer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentHomeBinding
import co.ke.mshirika.mshirika_app.MainActivity
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : MshirikaFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this

        with(requireActivity() as MainActivity) {
            onHomeAttached(binding.appBar.toolbar)
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            when (viewModel.status.value) {
                Status.Showing -> {
                    viewModel.update(Status.Hidden)
                    isEnabled = false
                }
                else -> {}
            }
        }
        viewModel.status.collectLatestLifecycle {
            when (it) {
                Status.Showing -> binding.apply {
                    callback.isEnabled = true
                    scrim.isVisible = true
                    componentExpansionTransition(
                        root as ViewGroup,
                        addFab,
                        additionCard,
                        resources.getDimension(R.dimen.elevation_addition_card)
                    ) {
                        additionCard.isVisible = true
                        addFab.isVisible = false
                    }
                }
                else -> binding.apply {
                    componentCollapsingTransition(
                        root as ViewGroup,
                        additionCard,
                        addFab,
                        resources.getDimension(R.dimen.elevation_addition_card)
                    ) {
                        additionCard.isVisible = false
                        scrim.isVisible = false
                        addFab.isVisible = true
                    }
                }
            }
        }
    }

    private fun navigateToSearch() {
        val card = getString(R.string.search_fragment_card)
        growingTransition(
            card,
            binding.searchCard,
            HomeFragmentDirections.actionHomeFragmentToSearchFragment()
        )
    }

    fun openSearchFragment() {
        navigateToSearch()
    }

    fun hideAddCard() {
        viewModel.update(Status.Hidden)
    }

    fun showAddCard() {
        Log.d(TAG, "showAddCard: clicked")
        viewModel.update(Status.Showing)
    }

    fun openCreateClient() {
        transition(
            R.string.create_client_transition,
            HomeFragmentDirections.actionGlobalCreateNewClientFragment()
        )
    }

    fun openCreateCenter() {
        transition(
            R.string.create_center_transition,
            HomeFragmentDirections.actionGlobalCreateCenterFragment()
        )
    }

    fun openCreateGroup() {
        transition(
            R.string.create_group_transition,
            HomeFragmentDirections.actionGlobalCreateGroupFragment()
        )
    }

    private fun transition(@StringRes transitionName: Int, navDirections: NavDirections) {
        hideAddCard()
        growingTransition(
            transitionName = getString(transitionName),
            view = binding.additionCard,
            navDirections = navDirections
        )
    }
}

private const val TAG = "HomeFragment"
