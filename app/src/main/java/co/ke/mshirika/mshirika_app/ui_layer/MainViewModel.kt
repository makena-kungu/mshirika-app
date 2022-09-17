package co.ke.mshirika.mshirika_app.ui_layer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.repositories.GlobalRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState
import co.ke.mshirika.mshirika_app.utility.connectivity.NetworkState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

//Use this viewModel in all the fragments so as to observe the network state
@HiltViewModel
class MainViewModel @Inject constructor(
    private val store: PreferencesStoreRepository,
    private val repo: GlobalRepo
) : ViewModel() {

    private val _netState = MutableStateFlow<NetworkState>(Empty)
    val netState = _netState.asSharedFlow()
    val staff = store.staff
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )
    val isSynced = store.isOffline.asLiveData()

    fun syncAndGoOnline() {
        viewModelScope.launch { store.syncAndGoOnline() }
    }

    fun goOffline() {
        viewModelScope.launch { store.goOffline() }
    }

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

    suspend fun authKey(): String {
        return store.authKey()
    }

    fun clearTables() {
        repo.clear()
    }
}