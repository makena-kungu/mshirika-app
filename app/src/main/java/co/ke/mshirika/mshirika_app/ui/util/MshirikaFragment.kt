package co.ke.mshirika.mshirika_app.ui.util

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *
 */
@AndroidEntryPoint
abstract class MshirikaFragment<B>(@LayoutRes contentLayoutId: Int) :
    Fragment(contentLayoutId),
    OnMenuItemClickListener {

    private var _binding: B? = null

    val binding get() = _binding!!
    val lifecycleScope get() = viewLifecycleOwner.lifecycleScope

    @Inject
    lateinit var authKey: String

    open val toolbar: Toolbar? = null
    open val toolbarTitle: String? = null

    @MenuRes
    open val resId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)

        toolbar?.setupToolbar()
    }

    private fun Toolbar.setupToolbar() {
        setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        resId?.let {
            setHasOptionsMenu(true)
            inflateMenu(it)
            setOnMenuItemClickListener(this@MshirikaFragment)
        }
        toolbarTitle?.let { title = it }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}