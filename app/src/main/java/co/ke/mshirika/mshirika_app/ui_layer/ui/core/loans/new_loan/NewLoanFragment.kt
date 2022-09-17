package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewLoanBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.NewLoanChargesFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.NewLoanDetailsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.new_loan.content.NewLoanTermsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.PageIndicatorAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.andd
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewLoanFragment : MshirikaFragment<FragmentCreateNewLoanBinding>(
    R.layout.fragment_create_new_loan
) {

    private val args by navArgs<NewLoanFragmentArgs>()
    private val viewModel by viewModels<NewLoanViewModel>()
    private val fragments = listOf<Fragment>(
        NewLoanDetailsFragment(),
        NewLoanChargesFragment(),
        NewLoanTermsFragment()
    )

    private val client get() = args.client
    private val toolbar get() = binding.appBar.toolbar
    private val viewPager get() = binding.newLoanAccountContainer

    private lateinit var adapter: PageIndicatorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.client = client
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setup(R.string.new_loan, R.string.details)
        viewModel.subtitle.collectLatestLifecycle {
            toolbar.setSubtitle(it)
        }
        viewModel.page.observe(viewLifecycleOwner) { page ->
            //the page has been updated do something
            var last = viewPager.adapter?.itemCount ?: return@observe
            last--

            val (isEnabled, icon, text) = when (page) {
                0 -> false andd (R.drawable.ic_round_navigate_next_24 to R.string.next)
                last -> true andd (R.drawable.ic_round_check_24 to R.string.submit)
                else -> true andd (R.drawable.ic_round_navigate_next_24 to R.string.next)
            }

            binding.goToPrevious.isEnabled = isEnabled
            binding.goToNext.setIconResource(icon)
            binding.goToNext.setText(text)
        }
        viewPager.apply {
            isUserInputEnabled = false
            registerOnPageChangeCallback(Pager())
            adapter = FormPagingAdapter(
                fragmentManager = childFragmentManager,
                lifecycle = fragmentLifecycle,
                fragments = fragments
            )
        }

        adapter = PageIndicatorAdapter(fragments.size)
        binding.indicators.adapter = adapter

        viewModel.created.observe(viewLifecycleOwner) {
            NewLoanFragmentDirections.actionNewLoanFragmentToGuarantorsFragment(
                clientName = client.displayName,
                clientId = it.clientId,
                loanId = it.clientId,
                guarantors = emptyArray()
            )
        }
    }

    fun navigateToNext() {
        viewPager.apply {
            adapter?.apply {
                navigateToNext1()
            }
        }
    }

    private fun navigateToNext1() {
        val maxIndex = viewPager.adapter!!.itemCount - 1
        val currentItem = viewPager.currentItem
        val found = childFragmentManager.findFragmentByTag("f${currentItem}")
        val fragment = found as ViewerFragment

        val cont = fragment.onNextPressed()
        if (!cont) return
        if (currentItem == maxIndex) submit()
    }

    private fun submit() {
        viewModel.createNewLoan(client.savingsAccountId)
    }

    fun navigateToPrev() {
        val page = viewPager.currentItem
        if (page > 0) viewPager.currentItem -= 1
    }

    inner class Pager : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            adapter.selectedPagePosition = position
            viewModel.update(position)
        }
    }
}