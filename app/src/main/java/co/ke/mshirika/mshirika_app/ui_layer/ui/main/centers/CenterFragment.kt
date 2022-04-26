package co.ke.mshirika.mshirika_app.ui_layer.ui.main.centers

import android.os.Bundle
import android.view.View
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentCenterBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.DetailsFragment
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CenterFragment : DetailsFragment<FragmentCenterBinding>(R.layout.fragment_center) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override val hasToolbar: Boolean
        get() = true
    override val toolbar: MaterialToolbar
        get() = binding.appBar.toolbarLarge
    override val toolbarTitle: String
        get() = getString(R.string.center)

}