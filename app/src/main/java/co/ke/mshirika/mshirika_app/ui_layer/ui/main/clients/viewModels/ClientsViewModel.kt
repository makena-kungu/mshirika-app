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
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State.Normal
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State.Searching
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repo: ClientsRepo,
    private val prefRepo: PreferencesStoreRepository,
    state: SavedStateHandle
) : MshirikaViewModel() {
    private val _clients = repo.clients.cachedIn(viewModelScope)
    private val _filteredClients = MutableStateFlow<PagingData<Client>>(empty())
    private val _modifiedFilteredClients = channelFlow<PagingData<Client>> {
        searchedClients.collectLatest {
            when (it) {
                is Outcome.Success -> it.data?.let { data -> send(data) }
                else -> send(empty())
            }
        }
    }
    private val _state = MutableStateFlow(state.getLiveData(STATE, DEFAULT).value ?: DEFAULT)

    private val searchedClients
        get() = repo.searchedClients

    val clients: StateFlow<PagingData<Client>>
        get() = _state.flatMapLatest {
            when (it) {
                Searching -> _modifiedFilteredClients
                Normal -> _clients
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = empty()
        )

    fun state(state: State = Normal) {
        _state.value = state
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
                data?.let {
                    _filteredClients.value = it
                }
            }
        }
    }

    suspend fun authKey(): String {
        return prefRepo.authKey()
    }

    companion object {
        private const val STATE = "current_state"
        private val DEFAULT: State = Normal
    }
}