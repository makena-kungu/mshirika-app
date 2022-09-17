package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentCreateNewClientBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.FamilyFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.GeneralFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.PageIndicatorAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.LoadingDialog
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import com.google.android.material.button.MaterialButton.ICON_GRAVITY_TEXT_END
import com.google.android.material.button.MaterialButton.ICON_GRAVITY_TEXT_START
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor() : MshirikaFragment<FragmentCreateNewClientBinding>(
    R.layout.fragment_create_new_client
) {

    private val viewModel: CreateClientViewModel by viewModels()
    private val toolbar by lazy { binding.appBar.toolbar }
    private val fragments: List<Fragment> = listOf(
        GeneralFragment(),
        FamilyFragment()
    )
    //PreviewFragment()
    //AddressFragment(),

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: PageIndicatorAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this
        toolbar.setup(R.string.create_client)
        viewModel.title.observe(viewLifecycleOwner) { resId ->
            toolbar.subtitle = getString(resId)
        }
        viewPager = binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = FormPagingAdapter(childFragmentManager, fragmentLifecycle, fragments)
            registerOnPageChangeCallback(PageChangeCallback())
        }

        adapter = PageIndicatorAdapter(fragments.size)
        binding.indicators.adapter = adapter
        binding.error()
        loading()
    }

    private fun FragmentCreateNewClientBinding.error() {
        viewModel.errorState.observe(viewLifecycleOwner) {
            root.snackL(it.text(requireContext())) {
                dismiss()
            }
        }
    }

    private fun loading() {
        val loading = LoadingDialog(requireContext()) {
            viewModel.cancel()
        }
        viewModel.loadingState.observe(viewLifecycleOwner) {
            if (it) loading.show()
            else loading.dismiss()
        }
    }

    fun navigateToNext() {
        Log.d(TAG, "navigateToNext: invoked")
        viewPager.apply {
            adapter?.apply {
                val maxIndex = itemCount - 1
                Log.d(TAG, "navigateToNext: maxIndex $maxIndex")
                when {
                    currentItem < maxIndex -> {
                        val found = childFragmentManager.findFragmentByTag("f$currentItem")
                        val fragment = found as ViewerFragment
                        val cont = fragment.onNextPressed()
                        Log.d(TAG, "navigateToNext: $cont")
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

    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Log.d(TAG, "onPageSelected: $position")
            adapter.selectedPagePosition = position

            viewPager.adapter?.let {
                binding.setup(position, it.itemCount - 1)
            }
        }

        private fun FragmentCreateNewClientBinding.setup(position: Int, last: Int) {
            val (isEnabled, icon, iconGravity, text) = when (position) {
                0 -> false to R.drawable.ic_round_navigate_next_24 pair (ICON_GRAVITY_TEXT_END to R.string.next)
                last -> true to R.drawable.ic_round_check_24 pair (ICON_GRAVITY_TEXT_START to R.string.submit)
                else -> true to R.drawable.ic_round_navigate_next_24 pair (ICON_GRAVITY_TEXT_END to R.string.next)
            }


            goToPrevious.isEnabled = isEnabled
            goToNext.iconGravity = iconGravity
            goToNext.setIconResource(icon)
            goToNext.setText(text)
        }

    }


    companion object {
        private const val TAG = "MainFragment"

        internal data class Quadruple<A, B, C, D>(
            val a: A,
            val b: B,
            val c: C,
            val d: D
        )

        internal infix fun <A, B, C, D> Pair<A, B>.pair(other: Pair<C, D>): Quadruple<A, B, C, D> {
            return Quadruple(first, second, other.first, other.second)
        }
    }
}