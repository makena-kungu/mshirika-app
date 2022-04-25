package co.ke.mshirika.mshirika_app.ui.util

import androidx.appcompat.app.AppCompatActivity
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

    suspend fun <T> Flow<T?>.collectLatestNonNull(collect: suspend (T) -> Unit) {
        collectLatest {
            it?.let { collect(it) }
        }
    }
}