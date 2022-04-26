package co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils

import android.os.Bundle
import android.view.View
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentPaymentBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : MshirikaFragment<FragmentPaymentBinding>(R.layout.fragment_payment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun submit() {

    }
}