package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentSendClientSmsBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.clear
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.snackL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@AndroidEntryPoint
class SendSmsFragment : MshirikaFragment<FragmentSendClientSmsBinding>(R.layout.fragment_send_client_sms) {

    private val viewModel by viewModels<SmsViewModel>()
    private val args by navArgs<SendSmsFragmentArgs>()
    private val clientId: Int by lazy { args.clientId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolbar.setup(R.string.send_sms)

        binding.sendSmsLayout.setEndIconOnClickListener {
            val msg = binding.sendSms.text()
            viewModel.sendSms(clientId, msg)
            binding.sendSms.clear()
        }

        binding.sendSms.doAfterTextChanged {
            binding.sendSmsLayout.setEndIconActivated(!it.isNullOrBlank())
        }

        viewModel.successState.observe(viewLifecycleOwner) {
            findNavController().apply {
                lifecycleScope.launch {
                    delay(1.seconds)
                    navigateUp()
                }
            }
        }

        viewModel.errorState.observe(viewLifecycleOwner) {
            binding.root.snackL(it.text(requireContext()))
        }
    }
}