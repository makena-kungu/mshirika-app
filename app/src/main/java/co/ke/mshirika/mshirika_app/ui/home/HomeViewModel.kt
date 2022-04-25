package co.ke.mshirika.mshirika_app.ui.home

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
        _status.value = status
    }
}

sealed class Status {
    object Hidden : Status()
    object Showing : Status()
}