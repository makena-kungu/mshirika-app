package co.ke.mshirika.mshirika_app.data_layer.remote_mediators

import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.age
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.LocalDate
import java.util.Calendar.*

class RemoteMediatorsTest {

    @Test
    fun `test whether the duration works`() {

        val now = getInstance()
        now.get(MINUTE)
        val then = getInstance()
        then.set(2022, MAY, 26, 23, 0, 0)


        println("then = ${then.time}")
        val duration = RemoteMediators.duration(then.timeInMillis)

        assertEquals(5, duration)
    }

    @Test
    fun `test whether the age calculator works`() {
        val then = getInstance()
        then.set(2000, JANUARY, 13)
        val age = then.timeInMillis.age
        assertEquals(age, 22)
    }

    @Test
    fun `test whether the java duration works`() {
        val then = getInstance()
        then.set(2000, JANUARY, 13)
        val duration = Duration.ofMillis(then.timeInMillis)
        val days = duration.toDays()
        println("days = ${LocalDate.ofEpochDay(days)}")
    }
}