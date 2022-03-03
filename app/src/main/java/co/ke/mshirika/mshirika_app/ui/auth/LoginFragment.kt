package co.ke.mshirika.mshirika_app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.response.Staff
import co.ke.mshirika.mshirika_app.databinding.DialogLoginLoadingBinding
import co.ke.mshirika.mshirika_app.databinding.FragmentLoginBinding
import co.ke.mshirika.mshirika_app.ui.MainActivity
import co.ke.mshirika.mshirika_app.utility.DataStore
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkMonitor
import co.ke.mshirika.mshirika_app.utility.network.Result
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.snackS
import co.ke.mshirika.mshirika_app.utility.ui.ViewUtils.text
import com.google.android.gms.common.util.Base64Utils.encode
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!
    private val dataStore by lazy { DataStore(requireContext()) }
    private val lifecycleScope = viewLifecycleOwner.lifecycleScope
    private val loadingDialog by lazy {
        MaterialAlertDialogBuilder(requireContext()).run {
            setCancelable(false)
            DialogLoginLoadingBinding
                .inflate(layoutInflater)
                .also {
                    setView(it.root)
                }
            create()
        }
    }
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.apply {
            fragment = this@LoginFragment
            lifecycleScope.launchWhenCreated {
                viewModel.auth.collectLatest { outcome ->
                    when (outcome) {
                        is co.ke.mshirika.mshirika_app.utility.network.Outcome.Result.Empty -> {
                            //do nothing
                        }
                        is co.ke.mshirika.mshirika_app.utility.network.Outcome.Result.Error -> {
                            TODO("display a snackbar")
                        }
                        is co.ke.mshirika.mshirika_app.utility.network.Outcome.Result.Loading -> {
                            //rather than showing the progress bar, Show A dialog with only a progressbar
                            // and that is non-dismissible
                            loadingDialog.show()
                            // TODO: check if I'm required to recreate the dialog once it's dismissed
                        }
                        is co.ke.mshirika.mshirika_app.utility.network.Outcome.Result.Success -> {
                            loadingDialog.apply {
                                outcome.handle()
                                if (isShowing) dismiss()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Result<Staff>.handle(): Boolean? = when (this) {
        is co.ke.mshirika.mshirika_app.utility.network.Outcome.Result.Empty -> null
        is co.ke.mshirika.mshirika_app.utility.network.Outcome.Result.Loading -> null
        is co.ke.mshirika.mshirika_app.utility.network.Outcome.Result.Error -> {
            msg.let { msg ->
                Snackbar
                    .make(
                        binding.root,
                        msg,
                        LENGTH_LONG
                    ).run {
                        //this ensures that when a user clicks retry, the progress
                        // is not hidden since the login sets the visibility as true
                        // which might then be reset by this return value
                        var retried = false
                        setAction(R.string.retry) {
                            login()
                            retried = true
                        }
                        show()
                        retried
                    }
            }
        }
        is co.ke.mshirika.mshirika_app.utility.network.Outcome.Result.Success -> {
            data?.apply {
                lifecycleScope.launchWhenCreated {
                    success(base64EncodedAuthenticationKey)
                }
            }
            false
        }
    }

    fun forgotPassword() {

    }

    fun login() {
        if (!networkMonitor.isOnline) {
            Snackbar.make(
                binding.root,
                getString(R.string.no_internet),
                LENGTH_LONG
            ).show()
            return
        }

        binding.apply {
            if (validate()) {
                //proceed
                val username = username.text()
                val password = password.text()

                viewModel.login(username, password)
                progressBar.isVisible = true
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun success(key: String) {
        encode(key.encodeToByteArray())?.let { authKey ->
            dataStore.saveAuthKey(authKey)
                .also {
                    if (it) {
                        proceed()
                        binding.root.snackS("Login Successful")
                    }
                }
        }
    }

    private fun proceed() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

}