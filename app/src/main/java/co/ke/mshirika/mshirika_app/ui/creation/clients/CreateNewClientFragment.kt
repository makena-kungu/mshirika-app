package co.ke.mshirika.mshirika_app.ui.creation.clients

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewClientBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.creation.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui.creation.FormPagingAdapter.Companion.PAGE_COUNT
import com.google.android.material.tabs.TabLayoutMediator

class CreateNewClientFragment :
    MshirikaFragment<FragmentCreateNewClientBinding>(R.layout.fragment_create_new_client) {

    private lateinit var viewPager: ViewPager2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.viewPager
        val tabLayout = binding.createClientProgress

        val adapter = FormPagingAdapter(parentFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.goToPrevious.isEnabled = false
                    }
                    PAGE_COUNT - 1 -> {
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

    fun navigateToNext() {
        viewPager.currentItem.let {
            if (it < PAGE_COUNT - 1)
                viewPager.currentItem = it + 1
        }
    }

    fun navigateToPrevious() {
        val page = viewPager.currentItem
        if (page > 0) viewPager.currentItem = page - 1
    }
}