package co.ke.mshirika.mshirika_app.ui_layer.ui.home

import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.now
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.Month
import java.time.MonthDay
import java.time.ZoneId
import java.util.*

class HomeFragmentTest {
    @Test
    fun `undoing of dates`() {
        val value = "June"
        println("data: raw = $value")
        Calendar.getInstance()
        //println("Date = $date")
        val comparison = compare(value)
        Assert.assertTrue(comparison)
    }

    fun compare(value: String): Boolean {
        val now = MonthDay.now()
        val then = getDate(value)?.let { Date(it) } ?: return false
        val temp = then.toInstant().atZone(ZoneId.systemDefault())
        val parse = MonthDay.from(temp)
        return parse.monthValue == now.monthValue
    }

    fun getDate(value: String): Long? {
        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
        val date = sdf.parse(value) ?: return null

        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal[Calendar.MONTH]
        cal.timeInMillis = now
        cal[Calendar.MONTH] = month
        return cal.timeInMillis
    }
}