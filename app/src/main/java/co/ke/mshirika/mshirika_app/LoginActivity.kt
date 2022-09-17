package co.ke.mshirika.mshirika_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.ke.mshirika.mshirika_app.databinding.ActivityLoginBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.findNavigationController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        finish()
    }
}