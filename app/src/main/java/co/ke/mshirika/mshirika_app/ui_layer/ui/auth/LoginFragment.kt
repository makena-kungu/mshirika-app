package co.ke.mshirika.mshirika_app.ui_layer.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentLoginBinding
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.FlowUtils.collectLatestFragmentLifecycle
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.LoadingDialog
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.hideKeyBoard
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackS
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkMonitor
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.loggedInState.collectLatest {
                if (it) {
                    Log.d(TAG, "onCreate: Is Logged In")
                    val authKey = viewModel.authKey.last()
                    Log.d(TAG, "onCreate: auth key = $authKey")
                    proceed()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadingDialog = LoadingDialog(requireContext()) { viewModel.cancel() }
        binding.fragment = this
        binding.apply {

            viewModel.auth.collectLatestFragmentLifecycle {
                viewModel.save(it.base64EncodedAuthenticationKey)
                viewModel.save(it)
                root.snackS(getString(R.string.login_successful))
                binding.signInButton.isEnabled = true
                proceed()
            }
            viewModel.errorState.collectLatestFragmentLifecycle {
                Snackbar.make(root, it.text(requireContext()), LENGTH_LONG).apply {
                    setAction(R.string.okay) { dismiss() }
                    show()
                }
            }
            viewModel.loadingState.collectLatestFragmentLifecycle {
                if (it) loadingDialog.show()
                else loadingDialog.dismiss()
            }
        }
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

                viewModel.login(username, password)
                //progressBar.isVisible = true
                signInButton.isEnabled = false
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

    private fun proceed() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

}