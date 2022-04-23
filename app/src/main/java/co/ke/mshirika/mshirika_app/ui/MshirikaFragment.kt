package co.ke.mshirika.mshirika_app.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.ui.util.OnMshirikaFragmentAttach
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale

/**
 *
 */
abstract class MshirikaFragment<B>(@LayoutRes contentLayoutId: Int) :
    Fragment(contentLayoutId),
    OnMenuItemClickListener {

    private var _binding: B? = null

    val binding get() = _binding!!
    val lifecycleScope get() = viewLifecycleOwner.lifecycleScope

    open val hasToolbar: Boolean = false
    open val isTopFragment: Boolean = false
    open val toolbar: MaterialToolbar? = null
    open val toolbarTitle: String? = null

    @MenuRes
    open val menuResId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)

        toolbar?.setupToolbar()
    }

    private fun MaterialToolbar.setupToolbar() {
        setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        menuResId?.let {
            setHasOptionsMenu(true)
            inflateMenu(it)
            setOnMenuItemClickListener(this@MshirikaFragment)
        }
        toolbarTitle?.let { title = it }

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    fun growingTransition(transitionName: String, view: View, navDirections: NavDirections) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }

        val extras = FragmentNavigatorExtras(view to transitionName)
        findNavController().navigate(navDirections, extras)
    }

    fun componentExpansionTransition(
        sceneRoot: ViewGroup,
        startV: View,
        endV: View,
        endElev: Float,
        action: () -> Unit
    ) {
        val transform = MaterialContainerTransform().apply {
            startView = startV
            endView = endV
            scrimColor = Color.TRANSPARENT
            endElevation = endElev
            addTarget(endV)
        }
        action()
        TransitionManager.beginDelayedTransition(sceneRoot, transform)
    }

    fun componentCollapsingTransition(
        sceneRoot: ViewGroup,
        startV: View,
        endV: View,
        startElev: Float,
        action: () -> Unit
    ) {
        val transform = MaterialContainerTransform().apply {
            startView = startV
            endView = endV
            scrimColor = Color.TRANSPARENT
            startElevation = startElev
            addTarget(endV)
        }
        action()
        TransitionManager.beginDelayedTransition(sceneRoot, transform)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // hide the activity's toolbar
        with(context as OnMshirikaFragmentAttach) {
            attach()
        }
    }

    override fun onDetach() {
        super.onDetach()
        with(context as OnMshirikaFragmentAttach) {
            detach()
        }
    }

    private fun OnMshirikaFragmentAttach.attach() {
        // if has it's own toolbar hide the parent otherwise don't hide the toolbar
        if (hidesToolbar)
            hideAppBar()
    }

    private fun OnMshirikaFragmentAttach.detach() {
        if (hidesToolbar)
            showAppBar()
    }

    private val hidesToolbar get() = isTopFragment && hasToolbar
}