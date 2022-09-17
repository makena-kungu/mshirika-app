package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentClientOtherDetailsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MoreFragment : MshirikaFragment<FragmentClientOtherDetailsBinding>(
    R.layout.fragment_client_other_details
) {

    private val viewModel by viewModels<MoreViewModel>()
    private val viewPager by lazy { binding.viewPager }
    private val args by navArgs<MoreFragmentArgs>()
    private val clientId by lazy { args.clientId }

    private lateinit var adapter: MorePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setClientId(clientId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setup(R.string.more)

        adapter = MorePagerAdapter(
            childFragmentManager,
            fragmentLifecycle
        )
        viewPager.adapter = adapter
        val list = listOf(
            R.string.family,
            R.string.next_of_kin,
            R.string.beneficiaries
        )
        val tabLayout = binding.clientsTabLayout

        for (i in list)
            tabLayout.addTab(tabLayout.newTab())

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(list[position])
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val id = list[position]
                val string = getString(id).lowercase(Locale.getDefault())
                val text = when (position) {
                    0 -> ""
                    else -> when (string) {
                        "beneficiaries" -> "beneficiary"
                        else -> string
                    }.let {
                        "Add $it"
                    }
                }
                binding.createSomething.text = text
            }
        })
    }

    fun createSomething() {
        val current = viewPager.currentItem
        val found = childFragmentManager.findFragmentByTag("f$current")
        val fragment = found as MshirikaFragment<*>
        fragment.executeInChild()
    }
}