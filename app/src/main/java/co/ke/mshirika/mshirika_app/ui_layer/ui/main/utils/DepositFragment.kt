package co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentDepositBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositFragment:Fragment(R.layout.fragment_deposit) {
    private var _binding:FragmentDepositBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDepositBinding.bind(view)
        binding.frag = this
    }

    fun submit() {

    }
}