package co.ke.mshirika.mshirika_app.ui.util

import java.text.DateFormat
import java.text.DateFormat.LONG
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    const val DATE_FORMAT = "dd MMMM yyyy"

    val String.fromLongDate: Long
        get() {
            return longDateFormat.parse(this)!!.time
        }

    val Long.longDate: String
        get() {
            return longDateFormat.format(Date(this))
        }

    private val longDateFormat: DateFormat
        get() {
            return DateFormat.getDateInstance(LONG)
        }

    val Long.mshirikaDate: String
        get() {
            return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(this))
        }
}