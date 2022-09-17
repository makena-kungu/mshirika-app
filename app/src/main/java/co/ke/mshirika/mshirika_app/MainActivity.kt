package co.ke.mshirika.mshirika_app

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ColorStateListDrawable
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.postDelayed
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.ke.mshirika.mshirika_app.databinding.ActivityMainBinding
import co.ke.mshirika.mshirika_app.databinding.LayoutNavHeaderBinding
import co.ke.mshirika.mshirika_app.ui_layer.MainViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.FlowUtils.collectLatestLifeCycleNonNull
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.FlowUtils.collectLatestLifecycle
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.findNavigationController
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkMonitor
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState.Offline
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState.Online
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var headerBinding: LayoutNavHeaderBinding
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var toggle: ActionBarDrawerToggle

    private val count = AtomicInteger(0)

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(toolbar)
            navController = findNavigationController(R.id.nav_host_fragment_main)
            appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
            //drawerLayout()

            internetStatus.internetStatus()
        }

        headerBinding = LayoutNavHeaderBinding.bind(
            binding
                .navView
                .getHeaderView(0)
        )

        viewModel.isSynced.observe(this) { isSynced ->
            // TODO: add offline mode indicator here or in the home fragment
        }

        networkMonitor.observe(this) {
            viewModel.changeNetworkState(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment_main)
            .navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    fun onHomeAttached(toolbar: MaterialToolbar) {
        binding.drawerLayout(toolbar)
    }

    private fun ActivityMainBinding.drawerLayout(toolbar: MaterialToolbar) {
        toggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(toggle)
        navView.setupWithNavController(navController)
        toggle.syncState()
        heading()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun TextView.internetStatus() {
        viewModel.netState.collectLatestLifecycle {
            when (this) {
                Online -> {
                    if (!isInitial) {
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
                    if (isInitial) {
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

    private fun heading() {
        if (count.getAndIncrement() > 0) return

        viewModel.staff.collectLatestLifeCycleNonNull {
            headerBinding.staff = it
        }
    }

    private fun ActivityMainBinding.hideNetStatus() {
        motionLayout.transitionToStart()
    }

    private fun ActivityMainBinding.showNetStatus() {
        motionLayout.transitionToEnd()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearTables()
    }

    private val Int.color: ColorStateList
        get() = AppCompatResources.getColorStateList(this@MainActivity, this)
}

