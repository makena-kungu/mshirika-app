package co.ke.mshirika.mshirika_app.ui.home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentHomeBinding
import co.ke.mshirika.mshirika_app.ui.util.MshirikaFragment
import com.google.android.material.transition.MaterialElevationScale
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
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }

        val card = getString(R.string.search_fragment_card)
        val extras = FragmentNavigatorExtras(binding.searchCard to card)
        HomeFragmentDirections.actionHomeFragmentToSearchFragment().also {
            findNavController().navigate(it, extras)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    fun openSearchFragment() {
        navigateToSearch()
    }
}
