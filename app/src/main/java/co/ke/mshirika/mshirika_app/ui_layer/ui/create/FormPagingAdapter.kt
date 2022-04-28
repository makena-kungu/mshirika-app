package co.ke.mshirika.mshirika_app.ui_layer.ui.create

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.*

class FormPagingAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]!!
    }

    companion object {
        const val PAGE_COUNT = 4

        @JvmStatic
        val fragments: Map<Int, Fragment>
            get() = mapOf(
                0 to GeneralFragment(),
                1 to FamilyFragment(),
                2 to AddressFragment(),
                3 to PreviewFragment()
            )
    }
}