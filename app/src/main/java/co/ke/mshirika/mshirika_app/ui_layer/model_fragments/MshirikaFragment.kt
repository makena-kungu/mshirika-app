package co.ke.mshirika.mshirika_app.ui_layer.model_fragments

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils.funga
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *
 */
abstract class MshirikaFragment<B>(@LayoutRes contentLayoutId: Int) :
    Fragment(contentLayoutId),
    OnMenuItemClickListener {

    var mBinding: B? = null
    val binding get() = mBinding!!
    val mLifecycle get() = viewLifecycleOwner.lifecycle
    val lifecycleScope get() = viewLifecycleOwner.lifecycleScope

    private val SearchView.searchCallback: OnBackPressedCallback
        get() = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (funga) isEnabled = false
        }

    private val _duration
        get() = resources
            .getInteger(R.integer.material_motion_duration_short_1).toLong()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)
    }

    fun MaterialToolbar.setupToolbar(
        @StringRes titleId: Int,
        @StringRes subtitleId: Int? = null,
        @MenuRes resId: Int? = null
    ) {
        setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        if (navigationIcon == null)
            navigationIcon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_round_arrow_back_24,
                null
            )
        resId?.let { menuResId ->
            setHasOptionsMenu(true)
            inflateMenu(menuResId)
        }
        setOnMenuItemClickListener(this@MshirikaFragment)
        setTitle(titleId)
        if (subtitleId == null) return
        setSubtitle(subtitleId)
    }

    fun SearchView.setup() {
        setOnFocusChangeListener { _, hasFocus ->
            searchCallback.isEnabled = hasFocus
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    fun growingTransition(transitionName: String, view: View, navDirections: NavDirections) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_medium_1).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_medium_1).toLong()
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
            duration = _duration
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
            duration = _duration
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
        mBinding = null
    }

    fun <T> Flow<T>.collectLatestLifecycle(collect: suspend (T) -> Unit) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectLatest(collect)
            }
        }
    }

    inline fun launch(crossinline block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                block()
            }
        }
    }

    inline fun <T> Flow<T?>.collectLatestLifeCycleNonNull(crossinline collect: suspend (T) -> Unit) {
        collectLatestLifecycle {
            it?.let { collect(it) }
        }
    }
}
