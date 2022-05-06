package co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewClientBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.PageIndicatorAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.content.AddressFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.content.FamilyFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.content.GeneralFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.content.PreviewFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.andd
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.LoadingDialog
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "MainFragment"

@AndroidEntryPoint
class MainFragment @Inject constructor() : MshirikaFragment<FragmentCreateNewClientBinding>(
    R.layout.fragment_create_new_client
) {

    private val viewModel: ViewModel by viewModels()
    private val fragments: List<Fragment> = listOf(
        GeneralFragment(),
        FamilyFragment(),
        AddressFragment(),
        PreviewFragment()
    )
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.appBar.toolbar.setupToolbar(R.string.create_client)
        viewModel.title.collectLatestLifecycle { resId ->
            binding.title.text = getString(resId)
        }
        viewPager = binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = FormPagingAdapter(parentFragmentManager, mLifecycle, fragments)
            registerOnPageChangeCallback(PageChangeCallback())
        }

        val adapter = PageIndicatorAdapter()
        binding.indicators.adapter = adapter
        viewModel.indicators.collectLatestLifecycle {
            Log.d(TAG, "onViewCreated: ${it.joinToString()}")
            adapter.submitList(it)
            adapter.notifyItemRangeChanged(0, it.count())
        }
        binding.error()
        loading()
    }

    private fun FragmentCreateNewClientBinding.error() {
        viewModel.errorState.collectLatestLifecycle {
            root.snackL(it.text(requireContext())) {
                dismiss()
            }
        }
    }

    private fun loading() {
        val loading = LoadingDialog(requireContext()) {
            viewModel.cancel()
        }
        viewModel.loadingState.collectLatestLifecycle {
            if (it) loading.show()
            else loading.dismiss()
        }
    }

    fun navigateToNext() {
        viewPager.apply {
            adapter?.apply {
                val maxIndex = itemCount - 1
                when {
                    currentItem < maxIndex -> {
                        val found = parentFragmentManager.findFragmentByTag("f$currentItem")
                        val fragment = found as ViewerFragment
                        fragment.onNextPressed()
                        currentItem += 1
                    }
                    currentItem == maxIndex -> {
                        getAddressAndSubmit()
                    }
                }
            }
        }
    }

    private fun getAddressAndSubmit() {
        viewModel.post()
    }

    fun navigateToPrevious() {
        val page = viewPager.currentItem
        if (page > 0) viewPager.currentItem = page - 1
    }

    fun onChildAttached() {
        // set up what the next and previous button does
    }

    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Log.d(TAG, "onPageSelected: $position")
            viewModel.updatePage(position)

            viewPager.adapter?.let {
                binding.setup(position, it.itemCount - 1)
            }
        }

        private fun FragmentCreateNewClientBinding.setup(position: Int, last: Int) {
            val (isEnabled, icon, text) = when (position) {
                0 -> false andd (R.drawable.ic_round_navigate_next_24 to R.string.next)
                last -> true andd (R.drawable.ic_round_check_24 to R.string.submit)
                else -> true andd (R.drawable.ic_round_navigate_next_24 to R.string.next)
            }

            goToPrevious.isEnabled = isEnabled
            goToNext.setIconResource(icon)
            goToNext.setText(text)
        }

    }
}