package co.ke.mshirika.mshirika_app.ui.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

/**
 * To be used in the activity's [AppCompatActivity.onCreate] method instead of the traditional
 * findNavController() method
 */
fun AppCompatActivity.findNavigationController(@IdRes id: Int): NavController =
    with(supportFragmentManager.findFragmentById(id) as NavHostFragment) {
        navController
    }