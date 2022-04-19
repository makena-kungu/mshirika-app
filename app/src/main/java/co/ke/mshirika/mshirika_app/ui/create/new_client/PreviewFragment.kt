package co.ke.mshirika.mshirika_app.ui.create.new_client

import android.os.Bundle
import android.view.View
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientPreviewBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.create.ViewerFragment

class PreviewFragment :
    MshirikaFragment<FragmentNewClientPreviewBinding>(R.layout.fragment_new_client_preview),ViewerFragment {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onNextPressed() {
        TODO("Not yet implemented")
    }
}