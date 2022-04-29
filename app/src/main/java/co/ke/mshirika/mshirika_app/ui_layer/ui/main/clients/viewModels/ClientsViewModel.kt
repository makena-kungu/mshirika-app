@file:OptIn(ExperimentalCoroutinesApi::class)

package co.ke.mshirika.mshirika_app.ui_layer.ui.main.clients.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingData.Companion.empty
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State.Normal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ClientsViewModel"

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repo: ClientsRepo,
    private val prefRepo: PreferencesStoreRepository,
    state: SavedStateHandle
) : MshirikaViewModel() {
    private val _filteredClients = MutableStateFlow<PagingData<Client>>(empty())
    private val _modifiedFilteredClients = channelFlow<PagingData<Client>> {
        searchedClients.collectLatest {
            when (it) {
                is Outcome.Success -> it.data?.let { data -> send(data) }
                else -> send(empty())
            }
        }
    }

    private val _stateChannel = Channel<State>()
    private val _state = MutableStateFlow(state.getLiveData(STATE, DEFAULT).value ?: DEFAULT)

    private val _expresar = channelFlow {
        _state.collectLatest {
            send(it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _clientes = _expresar.flatMapLatest {
        when (it) {
            Normal -> repo.clients.cachedIn(viewModelScope)
            else -> _modifiedFilteredClients
        }
    }

    val clientes get() = _clientes.stateIn(viewModelScope, WhileSubscribed(), empty())

    private val searchedClients
        get() = repo.searchedClients

    val clients = repo.clients.cachedIn(viewModelScope)

    fun clients() {
        viewModelScope.launch {
            /*Log.d(TAG, "clients: querying")
            repo.clients.apply {
                collectLatest {
                    clients.value = it
                }
            }*/
        }
    }

    fun state(state: State = Normal) {
        viewModelScope.launch {
            _stateChannel.send(state)
        }
    }

    fun search(query: String) {
        viewModelScope.launch { repo.search(query) }
    }

    init {
        viewModelScope.launch {
            handleSearchedClients()
        }
    }

    private suspend fun handleSearchedClients() {
        searchedClients.collectLatest { outcome ->
            outcome.stateHandler {
                _filteredClients.value = it
            }
        }
    }

    suspend fun authKey(): String {
        return prefRepo.authKey()
    }

    val authKey: Flow<String?> get() = prefRepo.authKey

    init {
        viewModelScope.launch {
            val value = state.getLiveData(STATE, DEFAULT).value ?: DEFAULT
            _stateChannel.send(value)
        }
        clients()
    }

    companion object {
        private const val STATE = "current_state"
        private val DEFAULT: State = Normal
    }
}