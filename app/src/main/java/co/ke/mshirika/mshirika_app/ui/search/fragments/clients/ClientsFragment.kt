package co.ke.mshirika.mshirika_app.ui.search.fragments.clients

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchFragsBinding
import co.ke.mshirika.mshirika_app.ui.main.client.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui.main.client.adapters.ClientsAdapter
import co.ke.mshirika.mshirika_app.ui.search.OnSearchListener
import co.ke.mshirika.mshirika_app.ui.search.SearchViewModel
import co.ke.mshirika.mshirika_app.utility.DataStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn

class ClientsFragment : Fragment(R.layout.fragment_search_frags), OnSearchListener {
    private var _binding: FragmentSearchFragsBinding? = null
    private var listener: OnClientItemClickListener? = null
    private var _authKey: String? = null

    private val binding get() = _binding!!
    private val lifecycleScope get() = viewLifecycleOwner.lifecycleScope
    private val vm by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(DataStore(requireContext())) {
            lifecycleScope.launchWhenStarted {
                _authKey = authKey.stateIn(lifecycleScope).value
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchFragsBinding.bind(view)

        _authKey?.let { authKey ->
            val adapter = ClientsAdapter(authKey, listener!!)
            binding.list.adapter = adapter
            lifecycleScope.launchWhenCreated {
                vm.clients.collectLatest {
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val title: String
        get() = getString(R.string.clients)

    companion object {
        fun getInstance(listener: OnClientItemClickListener): ClientsFragment {
            val fragment = ClientsFragment()
            fragment.listener = listener
            return fragment
        }
    }
}