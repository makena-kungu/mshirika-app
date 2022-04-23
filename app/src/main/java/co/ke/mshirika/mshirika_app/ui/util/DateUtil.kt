package co.ke.mshirika.mshirika_app.ui.util

import java.text.DateFormat
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    const val DATE_FORMAT = "dd MMMM yyyy"
    private const val REMOTE_DATE_FORMAT = "dd.MM.yyyy"


    val String.fromLongDate: Long
        get() {
            return longDateFormat.parse(this)!!.time
        }

    val String.fromShortDate: Long
        get() = shortDateFormat.parse(this)!!.time

    val Long.longDate: String
        get() {
            return longDateFormat.format(Date(this))
        }

    val Long.shortDate: String
        get() = shortDateFormat.format(Date(this))

    val Long.mediumDate: String
        get() = mediumDateFormat.format(Date(this))

    private val longDateFormat: DateFormat
        get() {
            return getDateInstance(LONG)
        }

    private val shortDateFormat: DateFormat
        get() {
            return getDateInstance(SHORT)
        }

    private val mediumDateFormat: DateFormat
        get() {
            return getDateInstance(MEDIUM)
        }

    val Long.mshirikaDate: String
        get() {
            return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(this))
        }


    val List<Int>.shortDate: String
        get() = shortDateFormat.format(date)
    val List<Int>.mediumDate: String
        get() = getDateInstance(MEDIUM).format(date)

    private val List<Int>.date: Long
        get() {
            val date = asReversed().joinToString(".")
            val sdf = SimpleDateFormat(REMOTE_DATE_FORMAT, Locale.getDefault())
            return sdf.parse(date)!!.time
        }

    fun mediumDate(date: List<Int>) = date.mediumDate
}