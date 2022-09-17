package co.ke.mshirika.mshirika_app.ui_layer.ui.util

import java.text.DateFormat
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

object DateUtil {
    const val DATE_FORMAT = "dd MMMM yyyy"
    private const val REMOTE_DATE_FORMAT = "dd.MM.yyyy"

    @JvmStatic
    val today: String
        get() = now.shortDate

    @JvmStatic
    val now: Long
        get() = System.currentTimeMillis()

    @JvmStatic
    val String.fromLongDate: Long
        get() = longDateFormat.parse(this)!!.time

    @JvmStatic
    val String.fromShortDate: Long
        get() = shortDateFormat.parse(this)!!.time

    @JvmStatic
    val Long.longDate: String
        get() = longDateFormat.format(Date(this))

    @JvmStatic
    val Long.shortDate: String
        get() = shortDateFormat.format(Date(this))

    @JvmStatic
    val Long.mediumDate: String
        get() = mediumDateFormat.format(Date(this))

    @JvmStatic
    private val longDateFormat: DateFormat
        get() = getDateInstance(LONG)

    private val shortDateFormat: DateFormat
        get() = getDateInstance(SHORT)

    private val mediumDateFormat: DateFormat
        get() = getDateInstance(MEDIUM)

    @JvmStatic
    val Long.mshirikaDate: String
        get() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(this))

    @JvmStatic
    val mshirikaDate: String = System.currentTimeMillis().mshirikaDate

    @JvmStatic
    val List<Int>.shortDate: String
        get() = shortDateFormat.format(date)

    @JvmStatic
    val List<Int>.mediumDate: String
        get() {
            return getDateInstance(MEDIUM).format(date)
        }

    @JvmStatic
    val List<Int>?.nullableMediumDate: String?
        get() {
            return this?.mediumDate
        }

    @JvmStatic
    val List<Int>.date: Long
        get() {
            val date = asReversed().joinToString(".")
            val sdf = SimpleDateFormat(REMOTE_DATE_FORMAT, Locale.getDefault())
            return sdf.parse(date)!!.time
        }

    @JvmStatic
    fun mediumDate(date: List<Int>) = date.mediumDate

    @JvmStatic
    val Long.age: Int
        get() {
            val duration = milliseconds.inWholeDays
            val then = LocalDate.ofEpochDay(duration)
            val now = LocalDate.now()

            return ChronoUnit.YEARS.between(then, now).toInt()
        }

    @JvmStatic
    private operator fun Year.minus(then: Year): Int {
        return value - then.value
    }
}