package co.ke.mshirika.mshirika_app.ui_layer.ui.util

import java.text.DateFormat
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.time.MonthDay
import java.time.Year
import java.util.*

object DateUtil {
    const val DATE_FORMAT = "dd MMMM yyyy"
    private const val REMOTE_DATE_FORMAT = "dd.MM.yyyy"


    val String.fromLongDate: Long
        get() = longDateFormat.parse(this)!!.time

    val String.fromShortDate: Long
        get() = shortDateFormat.parse(this)!!.time

    val Long.longDate: String
        get() = longDateFormat.format(Date(this))

    val Long.shortDate: String
        get() = shortDateFormat.format(Date(this))

    val Long.mediumDate: String
        get() = mediumDateFormat.format(Date(this))

    private val longDateFormat: DateFormat
        get() = getDateInstance(LONG)

    private val shortDateFormat: DateFormat
        get() = getDateInstance(SHORT)

    private val mediumDateFormat: DateFormat
        get() = getDateInstance(MEDIUM)

    val Long.mshirikaDate: String
        get() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(this))

    val mshirikaDate: String = System.currentTimeMillis().mshirikaDate

    val List<Int>.shortDate: String
        get() = shortDateFormat.format(date)
    val List<Int>.mediumDate: String
        get() = getDateInstance(MEDIUM).format(date)

    val List<Int>.date: Long
        get() {
            val date = asReversed().joinToString(".")
            val sdf = SimpleDateFormat(REMOTE_DATE_FORMAT, Locale.getDefault())
            return sdf.parse(date)!!.time
        }

    fun mediumDate(date: List<Int>) = date.mediumDate

    val Long.age: Int
        get() {
            val then = Calendar.getInstance()
            then.timeInMillis = this
            var age = Year.now() - Year.of(then[Calendar.YEAR])
            return with(MonthDay.now()) {
                val month = then[Calendar.MONTH]
                when {
                    month > monthValue -> age--
                    month == monthValue && then[Calendar.DAY_OF_MONTH] > dayOfMonth -> age--
                }
                age
            }
        }

    private operator fun Year.minus(then: Year): Int {
        return value - then.value
    }
}