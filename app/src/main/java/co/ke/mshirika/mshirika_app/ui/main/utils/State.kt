package co.ke.mshirika.mshirika_app.ui.main.utils

sealed class State {
    object Searching : State()
    object Normal : State()
}