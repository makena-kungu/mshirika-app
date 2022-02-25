package co.ke.mshirika.mshirika_app.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchBinding
import co.ke.mshirika.mshirika_app.ui.main.client.ClientsFragment
import co.ke.mshirika.mshirika_app.ui.main.group.GroupsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    val viewModel by viewModels<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!

    @Inject
    lateinit var clientsFragment: ClientsFragment

    @Inject
    lateinit var groupsFragment: GroupsFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        binding.apply {
            searchView.requestFocus()//request focus when the fragment is start
        }

    }

    private fun FragmentSearchBinding.setUpViewPager() {
        val adapter = ViewPagerAdapter(parentFragmentManager, lifecycle)
        viewPager.adapter = adapter
        adapter.addFragment(clientsFragment, groupsFragment)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}