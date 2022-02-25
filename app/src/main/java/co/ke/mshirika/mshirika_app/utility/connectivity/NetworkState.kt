package co.ke.mshirika.mshirika_app.utility.connectivity

sealed class NetworkState(var isInitial: Boolean = false) {
    object Online : NetworkState()
    object Offline : NetworkState()
    object Empty : NetworkState()
}