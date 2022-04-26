package co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils

sealed class State {
    object Searching : State()
    object Normal : State()
}