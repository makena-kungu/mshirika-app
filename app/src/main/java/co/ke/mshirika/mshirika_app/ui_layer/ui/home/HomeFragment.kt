package co.ke.mshirika.mshirika_app.ui_layer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import co.ke.mshirika.mshirika_app.MainActivity
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.dashboard.Statistics
import co.ke.mshirika.mshirika_app.databinding.FragmentHomeBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan.observeNonNull
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.amt
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.MonthDay
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class HomeFragment : MshirikaFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this

        toolbar = binding.appBar.toolbar
        with(requireActivity() as MainActivity) {
            onHomeAttached(toolbar)
        }

        handleOfflineMode()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            isEnabled = when (viewModel.status.value) {
                Status.Showing -> {
                    viewModel.update(Status.Hidden)
                    false
                }
                else -> {
                    false
                }
            }
        }
        viewModel.status.collectLatestLifecycle {
            when (it) {
                Status.Showing -> binding.apply {
                    callback.isEnabled = true
                    scrim.isVisible = true
                    componentExpansionTransition(
                        root as ViewGroup,
                        addFab,
                        additionCard,
                        resources.getDimension(R.dimen.elevation_addition_card)
                    ) {
                        additionCard.isVisible = true
                        addFab.isVisible = false
                    }
                }
                else -> binding.apply {
                    componentCollapsingTransition(
                        root as ViewGroup,
                        additionCard,
                        addFab,
                        resources.getDimension(R.dimen.elevation_addition_card)
                    ) {
                        additionCard.isVisible = false
                        scrim.isVisible = false
                        addFab.isVisible = true
                    }
                }
            }
        }

        statistics()
    }

    private fun handleOfflineMode() {
        toolbar.inflateMenu(R.menu.home)
        viewModel.isOffline.observe(viewLifecycleOwner) {
            val offlineModeItem = toolbar.menu.findItem(R.id.offline_mode)
            offlineModeItem.isVisible = it
        }
    }

    private fun statistics() {

        binding.day.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.update(TimeValue.Day)
        }
        binding.week.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.update(TimeValue.Week)
        }
        binding.month.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.update(TimeValue.Month)
        }

        viewModel.clientsDataset.observeNonNull(viewLifecycleOwner) { (time, stats) ->
            Log.d(TAG, "statistics: ${stats.joinToString()}")
            data(binding.clientsCount, time, stats)
        }

        viewModel.loansDataset.observeNonNull(viewLifecycleOwner) { (time, stats) ->
            data(binding.loansCount, time, stats)
        }

        binding.savingsCount.text = .0.amt
    }

    private fun data(texView: TextView, timeValue: TimeValue, stats: Statistics) {

        var text: String? = null
        for ((count, value) in stats) {
            val predicate: () -> Boolean = when (timeValue) {
                TimeValue.Day -> {
                    {
                        Log.d(TAG, "data: raw = $value")
                        val calendar = Calendar.getInstance()
                        val day = calendar[Calendar.DAY_OF_YEAR]
                        Log.d(TAG, "data: now = $day")
                        day == value.toInt()
                    }
                }
                TimeValue.Month -> {
                    {
                        compare(value)
                    }
                }
                else -> {
                    {
                        Log.d(TAG, "data: raw = $value")
                        val calendar = Calendar.getInstance()
                        val week = calendar[Calendar.WEEK_OF_YEAR]
                        Log.d(TAG, "data: week = $week")
                        week == value.toInt()
                    }
                }
            }

            if (predicate()) {
                text = "$count"
                break
            }
        }

        if (text == null) {
            text = "0"
        }
        texView.text = text
    }

    private fun compare(value: String): Boolean {
        val now = MonthDay.now()
        val then = getDate(value)?.let { Date(it) } ?: return false
        val temp = then.toInstant().atZone(ZoneId.systemDefault())
        val parse = MonthDay.from(temp)
        return parse.monthValue == now.monthValue
    }

    private fun getDate(value: String): Long? {
        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
        val date = sdf.parse(value) ?: return null

        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal[Calendar.MONTH]
        cal.timeInMillis = DateUtil.now
        cal[Calendar.MONTH] = month
        return cal.timeInMillis
    }

    private fun navigateToSearch() {
        val card = getString(R.string.search_fragment_card)
        growingTransition(
            card,
            binding.searchCard,
            HomeFragmentDirections.actionHomeFragmentToSearchFragment()
        )
    }

    fun openSearchFragment() {
        navigateToSearch()
    }

    fun hideAddCard() {
        viewModel.update(Status.Hidden)
    }

    fun showAddCard() {
        Log.d(TAG, "showAddCard: clicked")
        viewModel.update(Status.Showing)
    }

    fun openCreateClient() {
        transition(
            R.string.create_client_transition,
            HomeFragmentDirections.actionGlobalCreateNewClientFragment()
        )
    }

    fun openCreateCenter() {
        transition(
            R.string.create_center_transition,
            HomeFragmentDirections.actionGlobalCreateCenterFragment()
        )
    }

    fun openCreateGroup() {
        transition(
            R.string.create_group_transition,
            HomeFragmentDirections.actionGlobalCreateGroupFragment()
        )
    }

    private fun transition(@StringRes transitionName: Int, navDirections: NavDirections) {
        hideAddCard()
        growingTransition(
            transitionName = getString(transitionName),
            view = binding.additionCard,
            navDirections = navDirections
        )
    }
}

private const val TAG = "HomeFragment"
