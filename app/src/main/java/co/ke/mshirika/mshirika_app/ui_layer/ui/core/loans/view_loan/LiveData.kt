package co.ke.mshirika.mshirika_app.ui_layer.ui.core.loans.view_loan

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LiveData<T?>.observeNonNull(owner: LifecycleOwner, action: (T) -> Unit) {
    observe(owner) {
        it?.let(action)
    }
}