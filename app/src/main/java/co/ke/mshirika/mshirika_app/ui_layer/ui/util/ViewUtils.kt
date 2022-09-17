package co.ke.mshirika.mshirika_app.ui_layer.ui.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Size
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.EditableUtils.text
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.*
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.math.BigDecimal
import java.text.NumberFormat

object ViewUtils {

    //ensuring that the amount does not exceed 2 decimal places in an Edit text
    @JvmStatic
    val AMOUNT_REGEX: Regex
        get() = "\\d*\\.\\d{0,2}".toRegex()

    @JvmStatic
    val Double.amt: String
        get() {
            return NumberFormat.getCurrencyInstance().run {
                maximumFractionDigits = 0
                format(this@amt)
            }
        }

    @JvmStatic
    val BigDecimal.amt: String
        get() {
            return NumberFormat.getCurrencyInstance().run {
                maximumFractionDigits = 0
                format(this@amt)
            }
        }

    @JvmStatic
    private val Context.colors
        get() = run {
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
                    color(android.R.color.holo_red_light),
                    color(android.R.color.holo_red_dark)
                ),
                intArrayOf(
                    color(android.R.color.holo_orange_light),
                    color(android.R.color.holo_orange_dark)
                ),
                intArrayOf(
                    color(android.R.color.holo_blue_light),
                    color(android.R.color.holo_blue_dark)
                ),
                intArrayOf(
                    color(android.R.color.holo_green_light),
                    color(android.R.color.holo_green_dark)
                ),
                intArrayOf(
                    color(android.R.color.holo_red_dark),
                    color(android.R.color.holo_red_light)
                )
            )
        }

    @JvmStatic
    val Context.randomColors
        get() = colors.random()

    @JvmStatic
    fun View.hideKeyBoard() {
        val imm: InputMethodManager? = context.getSystemService()
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    @JvmStatic
    fun View.showKeyBoard() {
        val imm: InputMethodManager? = context.getSystemService()
        imm?.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    }

    @JvmStatic
    private fun Context.color(id: Int) = ResourcesCompat.getColor(resources, id, theme)

    @JvmStatic
    fun TextView.drawable(colorArray: IntArray) {
        background = Size(width, height).drawable(colorArray)
    }

    @JvmStatic
    fun ImageView.background(colorArray: IntArray) {
        background = Size(width, height).drawable(colorArray)
    }

    @JvmStatic
    fun Size.drawable(colorArray: IntArray): GradientDrawable {
        return GradientDrawable().apply {
            setSize(width, height)
            shape = GradientDrawable.OVAL
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.BOTTOM_TOP
            colors = colorArray
        }
    }

    @JvmStatic
    fun Size.drawable(colorArray: IntArray, drawable: Drawable) =
        LayerDrawable(arrayOf(drawable(colorArray), drawable))

    @JvmStatic
    val SearchView.funga: Boolean
        get() = if (!isIconified) {
            setQuery("", false)
            isIconified = true
            true
        } else false

    context (Fragment) @JvmStatic
    fun MaterialAutoCompleteTextView.setAdapter(list: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            android.R.id.text1,
            list
        )
        doAfterTextChanged {
            if (it == null) return@doAfterTextChanged
            EditableUtils
            if (it.text() !in list) {
                error = context.getString(R.string.invalid_input)
            }
        }
        setAdapter(adapter)
    }

    @JvmStatic
    fun View.snackS(message: String) =
        make(this, message, LENGTH_SHORT)

    @JvmStatic
    fun View.snackL(@StringRes messageId: Int) =
        make(this, messageId, LENGTH_LONG)

    @JvmStatic
    fun View.snackL(message: String) =
        make(this, message, LENGTH_LONG)

    @JvmStatic
    fun View.snackL(uiText: UIText) = make(this, uiText.text(context), LENGTH_LONG)

    @JvmStatic
    fun View.snackL(message: String, action: Snackbar.() -> Unit) {
        make(this, message, LENGTH_LONG, action)
    }

    @JvmStatic
    fun View.snackI(message: String) {
        return make(this, message, LENGTH_INDEFINITE) {
            setAction(R.string.okay) {
                dismiss()
            }
        }
    }

    @JvmStatic
    private fun make(
        view: View,
        @StringRes messageId: Int,
        @Suppress("SameParameterValue") @Duration duration: Int,
        action: Snackbar.() -> Unit = {}
    ) {
        return Snackbar.make(view, messageId, duration).apply {
            text
            action()
        }.show()
    }

    @JvmStatic
    private fun make(view: View, message: String, @Duration duration: Int, action: Snackbar.() -> Unit = {}) {
        Snackbar.make(view, message, duration).apply {
            text
            action()
        }.show()
    }

    @JvmStatic
    private val Snackbar.text: Snackbar
        get() {
            view.findViewById<TextView>(R.id.snackbar_text).maxLines = 5
            return this
        }
}