package co.ke.mshirika.mshirika_app.ui

import androidx.lifecycle.ViewModel
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

//Use this viewModel in all the fragments so as to observe the network state
class MainViewModel @Inject constructor() : ViewModel() {

    private val _netState = MutableStateFlow<NetworkState>(Empty)
    private var _key:String? = null
    val netState = _netState.asSharedFlow()

    fun updateKey(key:String) {
        _key =  key
    }

    val key get() = _key!!

    fun changeNetworkState(online: Boolean) {
        when (online) {
            true -> Online
            false -> Offline
        }.also {
            changeNetworkState(it)
        }
        //and the network state is already initialized
    }

    private fun changeNetworkState(state: NetworkState) {
        //when the state being observed is empty, init with isInitial = true.
        //otherwise just set the state to false to notify that it's not being set for the first time
        when (_netState.value) {
            Empty -> state.apply {
                if (isInitial)
                    isInitial = true
            }
            else -> state
        }.also {
            _netState.value = it
        }
    }
}