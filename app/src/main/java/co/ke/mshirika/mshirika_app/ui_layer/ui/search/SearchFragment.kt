package co.ke.mshirika.mshirika_app.ui_layer.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.getSystemService
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.LoanAccount
import co.ke.mshirika.mshirika_app.databinding.FragmentSearchBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.loans.OnLoanClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.OnClientItemClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.groups.OnGroupClickListener
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.clients.ClientsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.groups.GroupsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.search.fragments.loans.LoansFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailsTransition
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : MshirikaFragment<FragmentSearchBinding>(R.layout.fragment_search), OnClientItemClickListener,
    OnGroupClickListener, OnLoanClickListener {
    val viewModel by viewModels<SearchViewModel>()
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
            setupSearchView()
            setupViewPager()
        }

    }

    private fun FragmentSearchBinding.setupSearchView() {
        searchEditText.apply {
            requestFocus()
            searchLayout.setStartIconOnClickListener {
                findNavController().navigateUp()
            }
            setOnEditorActionListener { v, actionId, _ ->
                when (actionId) {
                    IME_ACTION_SEARCH -> v?.search() ?: false
                    else -> false
                }
            }
        }
    }

    private fun TextView.search(): Boolean {
        viewModel.setQuery(text.toString())
        //hide the keypad
        clearFocus()
        requireContext().getSystemService<InputMethodManager>()?.run {
            hideSoftInputFromWindow(windowToken, 0)
        }
        return true
    }

    private fun FragmentSearchBinding.setupViewPager() {
        val adapter = ViewPagerAdapter(parentFragmentManager, lifecycle)
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
        imageUrl: String,
        colors: IntArray
    ) {
        SearchFragmentDirections.actionGlobalClientFragment(
            client = client,
            clientImageUri = imageUrl,
            colors = colors
        ).also {
            itemToDetailsTransition(
                it
            )
        }
    }

    override fun onClickGroup(group: Group, position: Int) {
        // TODO: investigate if we need extras
        //itemToDetailTransition(it)
        TODO("Not yet implemented")
    }

    override fun onLoanClicked(loanAccount:LoanAccount) {
        SearchFragmentDirections.actionGlobalLoanRepaymentFragment().also {
            itemToDetailsTransition(it)
        }
        TODO("Not yet implemented")
    }

    override fun onLoanRepayClicked(
        loanAccount: LoanAccount,
        position: Int,
        container: View
    ): Boolean {
        TODO("Not yet implemented")
    }

    fun hideSuggestions() {
        binding.suggestionScrim.isVisible = false
    }

    override val toolbar: MaterialToolbar
        get() = binding.toolbar
    override val toolbarTitle: String
        get() = getString(R.string.search)

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
}