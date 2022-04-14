package co.ke.mshirika.mshirika_app.ui.util

import androidx.lifecycle.ViewModel
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class MshirikaViewModel : ViewModel() {
    val errorChannel = Channel<UIText>()
    val successChannel = Channel<String>()
    val loadingChannel = Channel<Boolean>()

    val loadingState get() = loadingChannel.receiveAsFlow()
    val errorState get() = errorChannel.receiveAsFlow()
    val successState get() = successChannel.receiveAsFlow()

    suspend inline fun <T> Outcome<T>.stateHandler(
        hasLoading: Boolean = true,
        success: Outcome.Success<T>.() -> Unit
    ) {
        stateHandlerWithAction(hasLoading = hasLoading, success = success) {}
    }

    suspend inline fun <T> Outcome<T>.stateHandlerWithAction(
        title: String? = null,
        hasLoading: Boolean = true,
        success: Outcome.Success<T>.() -> Unit,
        noinline action: () -> Unit = {}
    ) {
        when (this) {
            is Outcome.Empty -> false
            is Outcome.Loading -> hasLoading
            is Outcome.Error -> {
                errorChannel.send(dynamicText(msg, title, action = action))
                false
            }
            is Outcome.Success -> {
                success()
                false
            }
        }.also {
            loadingChannel.send(it)
        }
    }
}