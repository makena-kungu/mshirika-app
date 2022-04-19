package co.ke.mshirika.mshirika_app.ui.create.new_client

import android.os.Bundle
import android.view.View
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientAddressBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.create.ViewerFragment

class AddressFragment :
    MshirikaFragment<FragmentNewClientAddressBinding>(R.layout.fragment_new_client_address),
    ViewerFragment {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onNextPressed() {
        TODO("Not yet implemented")
    }
}