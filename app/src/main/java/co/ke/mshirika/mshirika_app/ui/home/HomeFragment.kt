package co.ke.mshirika.mshirika_app.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.navigation.NavDirections
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentHomeBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : MshirikaFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
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
        binding.apply {
            componentCollapsingTransition(
                root as ViewGroup,
                additionCard,
                addFab,
                resources.getDimension(R.dimen.elevation_addition_card)
            ) {
                additionCard.isVisible = false
            }
        }
    }

    fun showAddCard(view: View) {
        binding.apply {
            componentExpansionTransition(
                root as ViewGroup,
                view,
                additionCard,
                resources.getDimension(R.dimen.elevation_addition_card)
            ) {
                additionCard.isVisible = true
            }
        }
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
