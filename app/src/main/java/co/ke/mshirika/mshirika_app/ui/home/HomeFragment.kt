package co.ke.mshirika.mshirika_app.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentHomeBinding
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.apply {
            searchViews()
        }
    }

    private fun FragmentHomeBinding.searchViews() {

        searchView.setOnClickListener {
            navigateToSearch()
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object
}
