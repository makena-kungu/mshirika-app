package co.ke.mshirika.mshirika_app.ui.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import co.ke.mshirika.mshirika_app.R
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale

object Transitions {
    private val Fragment.mDuration
        get() = resources.getInteger(R.integer.material_motion_duration_medium_2).toLong()

    fun Fragment.itemToDetailsTransitionId(
        dirs: NavDirections,
        vararg sharedElements: Pair<View, Int>
    ) {
        val elements = sharedElements.map {
            it.first to getString(it.second)
        }
        itemToDetailsTransition(dirs, *elements.toTypedArray())
    }

    /**
     * Add this to the onItemClick() of the respective fragments
     */
    fun Fragment.itemToDetailsTransition(
        dirs: NavDirections,
        vararg sharedElements: Pair<View, String>
    ) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = mDuration
        }

        reenterTransition = MaterialElevationScale(true).apply {
            duration = mDuration
        }

        val extras = FragmentNavigatorExtras(
            *sharedElements
        )
        findNavController().navigate(directions = dirs, navigatorExtras = extras)
    }

    /**
     * Add this to the [Fragment.onViewCreated] of the list fragment
     */
    fun Fragment.itemToDetailReentry(view: View) {
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    /**
     * Add to the [Fragment.onCreate] in the receiver fragment
     */
    fun Fragment.itemToDetailSharedElementEnterTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_main
            duration = mDuration
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    /**
     * Retrieve a color from the current [android.content.res.Resources.Theme].
     */
    @ColorInt
    @SuppressLint("Recycle")
    fun Context.themeColor(
        @AttrRes themeAttrId: Int
    ): Int {
        return obtainStyledAttributes(
            intArrayOf(themeAttrId)
        ).use {
            it.getColor(0, Color.MAGENTA)
        }
    }
}