package co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewLoanBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.PageIndicatorAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.content.NewLoanChargesFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.content.NewLoanDetailsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_loan.content.NewLoanTermsFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.andd
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewLoanFragment : MshirikaFragment<FragmentCreateNewLoanBinding>(
    R.layout.fragment_create_new_loan
) {

    private val viewModel by viewModels<NewLoanViewModel>()
    private val fragments = listOf<Fragment>(
        NewLoanDetailsFragment(),
        NewLoanChargesFragment(),
        NewLoanTermsFragment()
    )
    private val toolbar get() = binding.appBar.toolbar
    private val viewPager get() = binding.newLoanAccountContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPages(fragments.size)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setupToolbar(R.string.new_loan, R.string.details)
        viewModel.subtitle.collectLatestLifecycle {
            toolbar.setSubtitle(it)
        }
        viewModel.enableNext.collectLatestLifecycle {
            binding.goToNext.isEnabled = it
        }
        viewModel.page.collectLatestLifecycle { page ->
            //the page has been updated do something
            var last = viewPager.adapter?.itemCount ?: return@collectLatestLifecycle
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
                fragmentManager = parentFragmentManager,
                lifecycle = mLifecycle,
                fragments = fragments
            )
        }

        val adapter = PageIndicatorAdapter()
        binding.indicators.adapter = adapter
        viewModel.indicators.collectLatestLifecycle {
            adapter.submitList(it)
            //todo check if it requires notifying
        }
    }

    fun navigateToNext() {
        viewPager.apply {
            adapter?.apply {
                binding.navigateToNext()
            }
        }
    }

    context (ViewPager2, RecyclerView.Adapter<RecyclerView.ViewHolder>) private
    fun FragmentCreateNewLoanBinding.navigateToNext() {
        val maxIndex = itemCount - 1
        val fragment = parentFragmentManager.findFragmentByTag("f${currentItem}")
        if (currentItem < maxIndex) with(fragment as ViewerFragment) { onNextPressed() }
        else submit()
    }

    private fun submit() {

    }

    fun navigateToPrev() {
        val page = viewPager.currentItem
        if (page > 0) viewPager.currentItem -= 1
    }

    inner class Pager : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.update(position)
        }
    }
}