package co.ke.mshirika.mshirika_app.ui_layer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.UIText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.dynamicText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class MshirikaViewModel : ViewModel() {
    val errorChannel = Channel<UIText>()
    val successChannel = Channel<UIText>()
    val loadingChannel = Channel<Boolean>(1)

    val loadingState get() = loadingChannel.receiveAsFlow()
    val errorState get() = errorChannel.receiveAsFlow()
    val successState get() = successChannel.receiveAsFlow()

    suspend inline fun <T> Outcome<T>.stateHandler(
        hasLoading: Boolean = true,
        success: (T) -> Unit
    ) {
        stateHandlerWithAction(hasLoading = hasLoading, success = success) {}
    }

    suspend inline fun <T> Outcome<T>.stateHandlerWithAction(
        title: String? = null,
        hasLoading: Boolean = true,
        success: (T) -> Unit,
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
                val outcome = this
                outcome.data?.let {
                    success(it)
                }
                false
            }
        }.also {
            loadingChannel.send(it)
        }
    }

    fun <T : MshirikaViewModel> T.launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    fun <T : MshirikaViewModel> T.launch(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context) {
            block()
        }
    }
}