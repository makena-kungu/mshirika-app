package co.ke.mshirika.mshirika_app.ui.create.new_client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewClientBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.create.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui.create.PageIndicatorAdapter
import kotlinx.coroutines.flow.collectLatest

class MainFragment :
    MshirikaFragment<FragmentCreateNewClientBinding>(R.layout.fragment_create_new_client) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.viewPager.apply {
            adapter = FormPagingAdapter(parentFragmentManager, lifecycle)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.updatePage(position)

                    when (position) {
                        0 -> {
                            binding.goToPrevious.isEnabled = false
                        }
                        FormPagingAdapter.PAGE_COUNT - 1 -> {
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
            })
        }

        PageIndicatorAdapter().also { indicatorAdapter ->
            lifecycleScope.launchWhenCreated {
                viewModel.indicators.collectLatest { it ->
                    indicatorAdapter.submitList(it)
                }

            }
            binding.indicators.adapter = indicatorAdapter
        }
    }

    fun navigateToNext() {
        viewPager.apply {
            adapter?.apply {
                val maxIndex = itemCount - 1
                when {
                    currentItem < maxIndex -> currentItem += 1
                    currentItem == maxIndex -> getAddressAndSubmit()
                }
            }
        }
    }

    private fun getAddressAndSubmit() {
        viewModel.submit()
    }

    fun navigateToPrevious() {
        val page = viewPager.currentItem
        if (page > 0) viewPager.currentItem = page - 1
    }

    fun onChildAttached() {
        // set up what the next and previous button does
    }
}