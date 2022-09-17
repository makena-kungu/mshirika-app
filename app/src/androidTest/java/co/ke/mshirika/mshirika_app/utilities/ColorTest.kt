package co.ke.mshirika.mshirika_app.utilities

import android.graphics.Color.GREEN
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ColorTest {

    @Test
    fun testWhetherTheContrastWorks() {
        val contrast = Cliente.contrast(GREEN)
        assert(contrast)
    }
}