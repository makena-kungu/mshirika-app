package co.ke.mshirika.mshirika_app.utility.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Size
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.*
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.text.DateFormat
import java.text.DateFormat.MEDIUM
import java.text.DateFormat.SHORT
import java.text.NumberFormat
import java.util.*

object ViewUtils {

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

    val List<Int>.date: Long
        get() {
            val year: Int = this[0]
            val month: Int = this[1]
            val date: Int = this[2]

            return with(Calendar.getInstance()) {
                set(year, month, date)
                timeInMillis
            }
        }
    val List<Int>.shortDate: String
        get() {
            val year: Int = this[0]
            val month: Int = this[1]
            val date: Int = this[2]

            return with(Calendar.getInstance()) {
                set(year, month, date)
                DateFormat.getDateInstance(SHORT).format(time)
            }
        }
    val List<Int>.mediumDate: String
        get() {
            val year: Int = this[0]
            val month: Int = this[1]
            val date: Int = this[2]

            return with(Calendar.getInstance()) {
                set(year, month, date)
                DateFormat.getDateInstance(MEDIUM).format(time)
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

    val Context.random get() = colors.random()

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

    /**
     * To be used in the activity's [AppCompatActivity.onCreate] method instead of the traditional
     * findNavController() method
     */
    fun AppCompatActivity.findNavigationController(@IdRes id: Int): NavController =
        with(supportFragmentManager.findFragmentById(id) as NavHostFragment) {
            navController
        }

    fun TextInputEditText.text(): String =
        text.toString().trim()

    fun View.snackS(message: String) =
        make(this, message, LENGTH_SHORT)
            .show()

    fun View.snackL(message: String) =
        make(this, message, LENGTH_LONG)
            .apply { show() }

    fun View.snackL(message: String, action: Snackbar.() -> Snackbar) {
        make(this, message, LENGTH_LONG).run {
            action()
            show()
        }
    }

    fun View.snackI(message: String) =
        make(this, message, LENGTH_INDEFINITE)


    fun mediumDate(date: List<Int>) = date.mediumDate
}