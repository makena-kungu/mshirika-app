package co.ke.mshirika.mshirika_app.ui_layer.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    private val _status = MutableStateFlow<Status>(Status.Hidden)
    val status get() = _status.asStateFlow()

    fun update(status: Status) {
        print(_status.value)
        _status.value = status
        print(_status.value)
    }

    private fun print(status: Status) {
        when (status) {
            Status.Hidden -> "Hidden"
            Status.Showing -> "Showing"
        }.also {
            Log.d(TAG, "print: $it")
        }
    }
}

private const val TAG = "HomeViewModel"

sealed class Status {
    object Hidden : Status()
    object Showing : Status()
}