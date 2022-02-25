package co.ke.mshirika.mshirika_app.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private val fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addFragment(vararg fragments: Fragment) {
        this.fragments += fragments

        fragments.forEach { fragment ->
            this.fragments.indexOf(fragment).also {
                notifyItemInserted(it)
            }
        }
    }
}