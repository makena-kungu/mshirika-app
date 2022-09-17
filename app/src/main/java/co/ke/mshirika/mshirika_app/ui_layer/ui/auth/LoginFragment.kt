package co.ke.mshirika.mshirika_app.ui_layer.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentLoginBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.auth.LoginFragmentDirections.Companion.actionLoginFragment2ToLoginLoadingFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.hideKeyBoard
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkMonitor
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : MshirikaFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
    }

    fun forgotPassword() {

    }

    fun login() {
        binding.apply {
            if (validate()) {
                //proceed
                root.hideKeyBoard()
                val username = username.text()
                val password = password.text()

                val direction = actionLoginFragment2ToLoginLoadingFragment(username, password)
                findNavController().navigate(direction)
            }
        }
    }

    private fun validate() =
        binding.run {
            username.isEmpty() && password.isEmpty()
        }.not()

    private fun TextInputEditText.isEmpty(): Boolean {
        return TextUtils.isEmpty(text).also {
            if (it) error = "This Field Is Required!"
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

}