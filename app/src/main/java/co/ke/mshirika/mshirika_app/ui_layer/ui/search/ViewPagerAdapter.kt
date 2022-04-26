package co.ke.mshirika.mshirika_app.ui_layer.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private val _fragments = mutableListOf<Fragment>()

    operator fun get(position: Int): Fragment {
        return _fragments[position]
    }

    override fun getItemCount(): Int {
        return _fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return _fragments[position]
    }

    fun addFragment(vararg fragments: Fragment) {
        _fragments += fragments

        fragments.forEach { fragment ->
            _fragments.indexOf(fragment).also {
                notifyItemInserted(it)
            }
        }
    }
}