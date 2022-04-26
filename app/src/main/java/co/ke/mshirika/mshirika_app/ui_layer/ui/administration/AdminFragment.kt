package co.ke.mshirika.mshirika_app.ui_layer.ui.administration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentAdminBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminFragment : Fragment(R.layout.fragment_admin) {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}