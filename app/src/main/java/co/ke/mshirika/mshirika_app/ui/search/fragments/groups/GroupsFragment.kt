package co.ke.mshirika.mshirika_app.ui.search.fragments.groups

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui.main.group.GroupsAdapter
import co.ke.mshirika.mshirika_app.ui.main.group.OnGroupClickListener
import co.ke.mshirika.mshirika_app.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui.search.SearchViewModel
import kotlinx.coroutines.flow.collectLatest

class GroupsFragment : Fragment(R.layout.fragment_search_frags), OnSearchListener {
    private var _binding: FragmentSearchFragsBinding? = null
    private var listener: OnGroupClickListener? = null

    private val binding get() = _binding!!
    private val lifecycleScope get() = viewLifecycleOwner.lifecycleScope
    private val vm by viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchFragsBinding.bind(view)
        val adapter = GroupsAdapter(listener!!)
        binding.list.adapter = adapter
        lifecycleScope.launchWhenCreated {
            vm.groups.collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val title: String
        get() = getString(R.string.groups)

    companion object {
        fun getInstance(listener: OnGroupClickListener): GroupsFragment {
            val fragment = GroupsFragment()
            fragment.listener = listener
            return fragment
        }
    }
}