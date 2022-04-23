package co.ke.mshirika.mshirika_app.ui.create.new_client

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientAddressBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui.util.Counties
import co.ke.mshirika.mshirika_app.ui.util.EditableUtils.text
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class AddressFragment :
    MshirikaFragment<FragmentNewClientAddressBinding>(R.layout.fragment_new_client_address),
    ViewerFragment {

    private val viewModel: ViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.county.setAdapter(Counties.counties)
    }

    private fun MaterialAutoCompleteTextView.setAdapter(list: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            android.R.id.text1,
            list
        )
        setAdapter(adapter)
    }

    override fun onNextPressed() {
        binding.apply {
            viewModel.saveAddress(
                Address(
                    address.text(),
                    postalCode.text().toInt(),
                    county.text()
                )
            )
        }
    }
}

data class Address(
    val address: String,
    val postalCode: Int,
    val county: String
)