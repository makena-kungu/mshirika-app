package co.ke.mshirika.mshirika_app.ui_layer.ui.administration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentBulkImportBinding

class BulkImportFragment:Fragment(R.layout.fragment_bulk_import) {

    private var _binding:FragmentBulkImportBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBulkImportBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}