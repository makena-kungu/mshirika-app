package co.ke.mshirika.mshirika_app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import co.ke.mshirika.mshirika_app.utility.DataStore
import co.ke.mshirika.mshirika_app.ui.MainActivity
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.ActivityLauncherBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            DataStore(this@LauncherActivity)
                .isLoggedIn
                .collectLatest {
                    if (it) startMainActivity()
                }
        }

        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_launcher)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}