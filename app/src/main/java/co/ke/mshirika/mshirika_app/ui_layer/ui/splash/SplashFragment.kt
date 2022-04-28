package co.ke.mshirika.mshirika_app.ui_layer.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSplashBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SplashFragment"

@AndroidEntryPoint
class SplashFragment : MshirikaFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.auth.collectLatestLifecycle {
            val navOptions = NavOptions.Builder()
            val direction = it?.let {
                navOptions.setPopUpTo(R.id.homeFragment, true)
                SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            } ?: kotlin.run {
                navOptions.setPopUpTo(R.id.loginFragment, true)
                SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            }

            findNavController().navigate(direction, navOptions.build())
        }
    }
}