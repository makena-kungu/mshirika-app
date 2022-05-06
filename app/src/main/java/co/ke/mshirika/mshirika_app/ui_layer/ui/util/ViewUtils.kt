package co.ke.mshirika.mshirika_app.ui_layer.ui.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Size
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.*
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.math.BigDecimal
import java.text.NumberFormat

object ViewUtils {

    //ensuring that the amount does not exceed 2 decimal places in an Edit text
    val AMOUNT_REGEX: Regex
        get() = "\\d*\\.\\d{0,2}".toRegex()

    private lateinit var res: Resources
    private lateinit var mTheme: Resources.Theme

    val Double.amt: String
        get() {
            return NumberFormat.getCurrencyInstance().run {
                maximumFractionDigits = 0
                format(this@amt)
            }
        }
    val BigDecimal.amt: String
        get() {
            return NumberFormat.getCurrencyInstance().run {
                maximumFractionDigits = 0
                format(this@amt)
            }
        }

    private val Context.colors
        get() = run {
            res = resources
            mTheme = theme
            arrayOf(
                intArrayOf(
                    color(android.R.color.holo_orange_dark),
                    color(android.R.color.holo_orange_light)
                ),
                intArrayOf(
                    color(android.R.color.holo_blue_dark),
                    color(android.R.color.holo_blue_light)
                ),
                intArrayOf(
                    color(android.R.color.holo_green_dark),
                    color(android.R.color.holo_green_light)
                ),
                intArrayOf(
                    color(android.R.color.holo_red_dark),
                    color(android.R.color.holo_red_light)
                )
            )
        }

    val Context.randomColors get() = colors.random()

    val View.hideKeyBoard: Unit
        get() {
            val imm: InputMethodManager? = context.getSystemService()
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }

    private fun color(id: Int) = ResourcesCompat.getColor(res, id, mTheme)

    fun TextView.drawable(colorArray: IntArray) {
        background = Size(width, height).drawable(colorArray)
    }

    fun Size.drawable(colorArray: IntArray): GradientDrawable {
        return GradientDrawable().apply {
            setSize(width, height)
            shape = GradientDrawable.OVAL
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.BOTTOM_TOP
            colors = colorArray
        }
    }

    fun Size.drawable(colorArray: IntArray, drawable: Drawable) =
        LayerDrawable(arrayOf(drawable(colorArray), drawable))

    val SearchView.funga: Boolean
        get() = if (!isIconified) {
            setQuery("", false)
            isIconified = true
            true
        } else false

    context (Fragment)
    fun MaterialAutoCompleteTextView.setAdapter(list: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            android.R.id.text1,
            list
        )
        setAdapter(adapter)
    }

    fun View.snackS(message: String) =
        make(this, message, LENGTH_SHORT)
            .show()

    fun View.snackL(message: String) =
        make(this, message, LENGTH_LONG)
            .apply { show() }

    fun View.snackL(message: String, action: Snackbar.() -> Unit) {
        make(this, message, LENGTH_LONG).run {
            action()
            show()
        }
    }

    fun View.snackI(message: String) =
        make(this, message, LENGTH_INDEFINITE)
}