package co.ke.mshirika.mshirika_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun more(item:MenuItem) {
        // TODO: 19/01/2022 show a bottom sheet dialog here with a menu
    }
}