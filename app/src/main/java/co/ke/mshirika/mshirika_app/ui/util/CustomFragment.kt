package co.ke.mshirika.mshirika_app.ui.util

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

/**
 *
 */
abstract class CustomFragment<B>(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    private var _binding: B? = null

    val binding get() = _binding!!
    val lifecycleScope get() = viewLifecycleOwner.lifecycleScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}