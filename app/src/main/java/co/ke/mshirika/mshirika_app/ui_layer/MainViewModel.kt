package co.ke.mshirika.mshirika_app.ui_layer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

//Use this viewModel in all the fragments so as to observe the network state
@HiltViewModel
class MainViewModel @Inject constructor(
    prefs: PreferencesStoreRepository
) : ViewModel() {

    private val _netState = MutableStateFlow<NetworkState>(Empty)
    val netState = _netState.asSharedFlow()
    val staff = flow {
        withContext(IO) {
            val staff = prefs.staff()
            emit(staff)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

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