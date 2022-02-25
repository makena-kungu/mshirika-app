package co.ke.mshirika.mshirika_app.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentHomeBinding
import co.ke.mshirika.mshirika_app.ui.home.HomeFragment.State.NORMAL
import co.ke.mshirika.mshirika_app.ui.home.HomeFragment.State.SEARCHING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val _activity by lazy { requireActivity() as AppCompatActivity }
    private val viewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var authKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        _activity.toolBar()

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.state.collectLatest {
                    when (it) {
                        SEARCHING -> stateSearching()
                        NORMAL -> stateNormal()
                    }
                }
            }
            //drawerLayout()
            searchViews()
        }
    }

    private fun AppCompatActivity.toolBar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun FragmentHomeBinding.searchViews() {
        searchView.apply {
            setOnSearchClickListener {
                onSearchFocus()
            }
            setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) onSearchFocus()
            }

            setOnCloseListener {
                viewModel.changeState(NORMAL)
                false
            }
        }
    }

    private fun onSearchFocus() {
        //first hide the necessary views
        findNavController()
            .navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            )
    }

    private fun FragmentHomeBinding.stateSearching() {
        //updateViews(false)
    }

    private fun FragmentHomeBinding.stateNormal() {
        //updateViews(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class State {
        SEARCHING,
        NORMAL
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}
