package co.ke.mshirika.mshirika_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import co.ke.mshirika.mshirika_app.databinding.ActivityAuthBinding
import co.ke.mshirika.mshirika_app.ui_layer.AuthViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.FlowUtils.collectLatestLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isAuthenticated.collectLatestLifecycle {
            if (this) {
                // open main activity
                startMainActivity()
            } else {
                //open login activity
                startLoginActivity()
            }
        }
    }

    private fun startMainActivity() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
    }

    private fun startLoginActivity() {
        val loginActivity = Intent(this, LoginActivity::class.java)
        startActivity(loginActivity)
    }

    override fun startActivity(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        super.startActivity(intent)
    }
}