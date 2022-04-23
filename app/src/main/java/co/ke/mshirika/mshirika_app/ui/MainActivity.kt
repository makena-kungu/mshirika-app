package co.ke.mshirika.mshirika_app.ui

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ColorStateListDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionManager.beginDelayedTransition
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.ActivityMainBinding
import co.ke.mshirika.mshirika_app.ui.util.OnMshirikaFragmentAttach
import co.ke.mshirika.mshirika_app.ui.util.findNavigationController
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkMonitor
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState.Offline
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState.Online
import com.google.android.material.navigation.NavigationView
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.MaterialSharedAxis.Y
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.Duration
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    Toolbar.OnMenuItemClickListener,
    NavigationView.OnNavigationItemSelectedListener,
    OnMshirikaFragmentAttach {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var toggle: ActionBarDrawerToggle

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val appBarListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            binding.appBar.apply {
                elevation =
                    when (destination.id) {
                        R.id.homeFragment -> 0F
                        else -> resources.getDimension(R.dimen.appbar_elevation)
                    }

                isVisible =
                    when (destination.id) {
                        R.id.clientFragment -> false
                        R.id.searchFragment -> false
                        else -> true
                    }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(toolbar)
            navController = findNavigationController(R.id.nav_host_fragment_main)
            appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
            drawerLayout()
            navController.addOnDestinationChangedListener(appBarListener)

            internetStatus.internetStatus()
        }

        networkMonitor.observe(this) {
            viewModel.changeNetworkState(it)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        binding.root
        TODO("Not yet implemented")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment_main)
            .navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    private fun ActivityMainBinding.drawerLayout() {
        toggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_homeFragment_to_clientFragment -> {}
            }
            true
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun TextView.internetStatus() {
        lifecycleScope.launchWhenCreated {
            viewModel.netState.collect {
                when (it) {
                    Online -> {
                        if (!it.isInitial) {
                            //decorate regardless of whether it's online or not
                            decorate(
                                R.string.online,
                                R.color.green,
                                R.color.colorInternetStatusTextOnline
                            )
                            binding.currentState()

                            Duration
                                .ofSeconds(2)
                                .toMillis()
                                .also { duration ->
                                    postDelayed(duration) {
                                        // hide the view after a short period of two seconds
                                        // regardless of whether it was initially online or not.
                                        // Therefore the hideNetStatus is only called when the user is back
                                        // online.
                                        binding.hideNetStatus()
                                    }
                                }
                        }
                    }
                    Offline -> {
                        if (it.isInitial) {
                            decorate(
                                R.string.offline,
                                R.color.stop,
                                R.color.colorInternetStatusTextOffline
                            )
                        } else {
                            decorate(
                                R.string.lost_connectivity,
                                R.color.no_internet,
                                R.color.white
                            )
                        }

                        binding.currentState()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun TextView.decorate(resId: Int, backgroundColorId: Int, foregroundId: Int) {
        text = getString(resId)
        background = backgroundColorId.color.let {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> ColorStateListDrawable(it)
                else -> ColorDrawable(it.defaultColor)
            }
        }

        setTextColor(foregroundId.color)
    }

    private fun ActivityMainBinding.currentState() {
        motionLayout.apply {
            //start state is mapped to hidden
            //so when online not initial show the view if its hidden
            //otherwise if it
            if (currentState == startState) showNetStatus()
        }
    }

    private fun ActivityMainBinding.hideNetStatus() {
        motionLayout.transitionToStart()
    }

    private fun ActivityMainBinding.showNetStatus() {
        motionLayout.transitionToEnd()
    }

    private val Int.color: ColorStateList
        get() = AppCompatResources.getColorStateList(this@MainActivity, this)

    override fun onResume() {
        super.onResume()
        navController.apply {
            addOnDestinationChangedListener(appBarListener)
        }
    }

    override fun onPause() {
        super.onPause()
        navController.apply {
            removeOnDestinationChangedListener(appBarListener)
        }
    }

    override fun hideAppBar(hide: Boolean) {
        MaterialSharedAxis(Y, hide).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_medium_2).toLong()
            binding.appBar.apply {
                addTarget(this)
                isVisible = !hide
            }
        }.also {
            beginDelayedTransition(binding.root, it)
        }
    }
}

