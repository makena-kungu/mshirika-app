package co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewClientBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.PageIndicatorAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.LoadingDialog
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor() :
    MshirikaFragment<FragmentCreateNewClientBinding>(R.layout.fragment_create_new_client) {

    private val viewModel: ViewModel by viewModels()
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.viewPager.apply {
            adapter = FormPagingAdapter(
                parentFragmentManager,
                lifecycle
            )
            registerOnPageChangeCallback(PageChangeCallback())
        }

        PageIndicatorAdapter().also { indicatorAdapter ->
            lifecycleScope.launchWhenCreated {
                viewModel.indicators.collectLatest {
                    indicatorAdapter.submitList(it)
                }
                binding.error()
                loading()
            }
            binding.indicators.adapter = indicatorAdapter
        }

    }

    private suspend fun FragmentCreateNewClientBinding.error() {
        viewModel.errorState.collect {
            root.snackL(it.text(requireContext())) {
                dismiss()
            }
        }
    }

    private suspend fun loading() {
        val loading = LoadingDialog(requireContext()) {
            viewModel.cancel()
        }
        viewModel.loadingState.collect {
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
                        with(
                            parentFragmentManager
                                .findFragmentByTag("f$currentItem") as ViewerFragment
                        ) {
                            onNextPressed()
                        }
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

    override val hasToolbar: Boolean
        get() = true
    override val isTopFragment: Boolean
        get() = true
    override val toolbar: MaterialToolbar
        get() = binding.appBarLarge.toolbarLarge
    override val toolbarTitle: String
        get() = getString(R.string.create_client)

    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.updatePage(position)

            when (position) {
                0 -> {
                    binding.goToPrevious.isEnabled = false
                }
                viewPager.adapter!!.itemCount - 1 -> {
                    binding.goToNext.apply {
                        setIconResource(R.drawable.ic_round_check_24)
                        setText(R.string.submit)
                    }
                }
                else -> {
                    binding.goToPrevious.isEnabled = true
                    binding.goToNext.apply {
                        setIconResource(R.drawable.ic_round_navigate_next_24)
                        setText(R.string.next)
                    }
                }
            }
        }

    }
}