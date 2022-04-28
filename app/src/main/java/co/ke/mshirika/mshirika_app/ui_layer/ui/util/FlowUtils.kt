package co.ke.mshirika.mshirika_app.ui_layer.ui.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object FlowUtils {
    context (AppCompatActivity)
    fun <T> Flow<T>.collectLatestLifecycle(collect: suspend T.() -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectLatest(collect)
            }
        }
    }

    context (AppCompatActivity)
    fun <T> Flow<T?>.collectLatestLifeCycleNonNull(collect: suspend (T) -> Unit) {
        collectLatestLifecycle {
            this?.let {
                collect(it)
            }
        }
    }

    context (Fragment)
    fun <T> Flow<T>.collectLatestFragmentLifecycle(collect: suspend (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectLatest(collect)
            }
        }
    }

    suspend fun <T> Flow<T?>.collectLatestNonNull(collect: suspend (T) -> Unit) {
        collectLatest {
            it?.let { collect(it) }
        }
    }
}