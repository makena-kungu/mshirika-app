package co.ke.mshirika.mshirika_app.ui.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class MshirikaViewModel : ViewModel() {
    val errorChannel = Channel<UIText>()
    val loadingChannel = Channel<Boolean>()

    val loadingState get() = loadingChannel.receiveAsFlow()
    val errorState get() = errorChannel.receiveAsFlow()
}