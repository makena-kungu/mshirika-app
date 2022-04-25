package co.ke.mshirika.mshirika_app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentLoginBinding
import co.ke.mshirika.mshirika_app.ui.MainActivity
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui.util.LoadingDialog
import co.ke.mshirika.mshirika_app.ui.util.ViewUtils.snackS
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkMonitor
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
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
        val loadingDialog = LoadingDialog(requireContext()) { viewModel.cancel() }
        binding.fragment = this
        binding.apply {

            viewModel.auth.collectLatestLifecycle {
                viewModel.save(it.base64EncodedAuthenticationKey)
                viewModel.save(it)
                root.snackS(getString(R.string.login_successful))
                proceed()
            }
            viewModel.errorState.collectLatestLifecycle {
                Snackbar.make(root, it.text(requireContext()), LENGTH_LONG).apply {
                    setAction(R.string.okay) { dismiss() }
                    show()
                }
            }
            viewModel.loadingState.collectLatestLifecycle {
                if (it) loadingDialog.show()
                else loadingDialog.dismiss()
            }
        }
    }

    fun forgotPassword() {

    }

    fun login() {
        /*if (!networkMonitor.isOnline) {
            Snackbar.make(
                binding.root,
                getString(R.string.no_internet),
                LENGTH_LONG
            ).show()
            return
        }*/
        binding.apply {
            if (validate()) {
                //proceed
                val username = username.text()
                val password = password.text()

                viewModel.login(username, password)
                //progressBar.isVisible = true
                signInButton.isEnabled = false
            }
        }
    }

    private fun validate() =
        !binding.run {
            username.isEmpty() && password.isEmpty()
        }

    private fun TextInputEditText.isEmpty(): Boolean {
        return TextUtils.isEmpty(text).also {
            if (it) error = "This Field Is Required!"
        }
    }

    private fun proceed() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

}