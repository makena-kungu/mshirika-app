package co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils

sealed class State {
    object Searching : State()
    object Normal : State()
    object Offline : State()
}