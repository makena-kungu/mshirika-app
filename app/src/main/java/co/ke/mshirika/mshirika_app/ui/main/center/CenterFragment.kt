package co.ke.mshirika.mshirika_app.ui.main.center

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentCenterBinding
import co.ke.mshirika.mshirika_app.ui.util.Transitions.itemToDetailSharedElementEnterTransition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CenterFragment : Fragment(R.layout.fragment_center) {
    private var _bind: FragmentCenterBinding? = null
    private val bind get() = _bind!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemToDetailSharedElementEnterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bind = FragmentCenterBinding.bind(view)
        bind.appBar.toolbarLarge.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}