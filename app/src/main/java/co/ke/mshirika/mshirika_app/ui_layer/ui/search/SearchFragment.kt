package co.ke.mshirika.mshirika_app.ui_layer.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups.OnGroupClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.LoansFragmentDirections
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.OnLoanClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.clients.ClientsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.groups.GroupsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.loans.LoansFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailsTransition
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.hideKeyBoard
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.showKeyBoard
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : MshirikaFragment<FragmentSearchBinding>(
    R.layout.fragment_search
), OnClientItemClickListener, OnGroupClickListener, OnLoanClickListener {
    private val viewModel by viewModels<SearchViewModel>()

    private val clientsFragment by lazy {
        ClientsFragment.getInstance(this)
    }
    private val groupsFragment by lazy {
        GroupsFragment.getInstance(this)
    }
    private val loansFragment by lazy {
        LoansFragment.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_main
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            root.showKeyBoard()
            setupSearchView()
            setupViewPager()
        }

        viewModel.loadingState.collectLatestLifecycle {
            binding.searchLoading.isVisible = it
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val query: String = _query ?: return
                search(position, query)
            }
        })
    }

    private fun FragmentSearchBinding.setupSearchView() {
        searchEditText.apply {
            requestFocus()
            searchLayout.setStartIconOnClickListener {
                findNavController().navigateUp()
            }
            setOnEditorActionListener { v, actionId, _ ->
                when (actionId) {
                    IME_ACTION_SEARCH -> {
                        v?.search() ?: false
                    }
                    else -> false
                }
            }
        }
    }

    private fun TextView.search(): Boolean {
        val query = text.toString()

        if (query.isEmpty()) return false

        //viewModel.setQuery(query)
        //hide the keypad

        val currentItem = binding.viewPager.currentItem
        _query = query
        search(currentItem, query)
        clearFocus()
        hideKeyBoard()
        return true
    }

    private fun search(position: Int, query: String) {
        val found = childFragmentManager.findFragmentByTag("f$position") ?: return
        val fragment = found as OnSearchListener
        Log.d(TAG, "search: position = $position")
        fragment.search(query)
    }

    private var _query: String? = null

    private fun FragmentSearchBinding.setupViewPager() {
        val adapter = SearchingPagerAdapter(childFragmentManager, fragmentLifecycle)
        viewPager.adapter = adapter
        adapter.addFragment(
            clientsFragment,
            groupsFragment,
            loansFragment
        )
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (adapter[position]) {
                is GroupsFragment -> R.string.groups
                is LoansFragment -> R.string.loans
                is ClientsFragment -> R.string.clients
                else -> null
            }?.also { resId ->
                tab.text = getString(resId)
            }
        }.attach()
    }

    override fun onClickClient(
        containerView: View,
        client: Client,
        imageUrl: String?,
        colors: IntArray?
    ) {
        SearchFragmentDirections.actionGlobalClientFragment(
            client = client,
            clientImageUri = imageUrl,
            colors = colors
        ).also {
            itemToDetailsTransition(it)
        }
    }

    override fun onClickGroup(group: Group, position: Int) {
        // TODO: investigate if we need extras
        //itemToDetailTransition(it)
        TODO("Not yet implemented")
    }

    override fun onLoanClicked(
        loanAccount: ConservativeLoanAccount,
        position: Int,
        container: View
    ) {
        TODO("Not yet implemented")
    }

    override fun onLoanRepayClicked(
        loanAccount: ConservativeLoanAccount,
        position: Int,
        container: View
    ): Boolean {
        val dirs = LoansFragmentDirections.actionGlobalLoanRepaymentFragment(
            clientName = loanAccount.clientName,
            clientId = loanAccount.clientId,
            loanId = loanAccount.id
        )
            findNavController().navigate(dirs)
        return true
    }

    fun hideSuggestions() {
        binding.suggestionScrim.isVisible = false
    }

    @ColorInt
    @SuppressLint("Recycle")
    fun Context.themeColor(
        @AttrRes themeAttrId: Int
    ): Int {
        return obtainStyledAttributes(
            intArrayOf(themeAttrId)
        ).use {
            it.getColor(0, Color.MAGENTA)
        }
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}