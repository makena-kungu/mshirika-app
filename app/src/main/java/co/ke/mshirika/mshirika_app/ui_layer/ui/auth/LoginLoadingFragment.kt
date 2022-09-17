package co.ke.mshirika.mshirika_app.ui_layer.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.MainActivity
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentLoginLoadingBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.UIText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginLoadingFragment : MshirikaFragment<FragmentLoginLoadingBinding>(R.layout.fragment_login_loading) {
    private val viewModel: LoginViewModel by viewModels()
    private val args: LoginLoadingFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.login(args.username, args.password)
        viewModel.errorState.observe(viewLifecycleOwner) {
            error(it)
        }

        viewModel.successState.observe(viewLifecycleOwner) {
            success(it)
        }
    }

    fun error(uiText: UIText) {
        binding.root.snackL(uiText)
        findNavController().popBackStack()
    }

    private fun success(uiText: UIText) {
        binding.root.snackL(uiText)
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}