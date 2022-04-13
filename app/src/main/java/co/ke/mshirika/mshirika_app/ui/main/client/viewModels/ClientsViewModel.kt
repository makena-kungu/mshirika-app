@file:OptIn(ExperimentalCoroutinesApi::class)

package co.ke.mshirika.mshirika_app.ui.main.client.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingData.Companion.empty
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Normal
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Searching
import co.ke.mshirika.mshirika_app.ui.util.MshirikaViewModel
import co.ke.mshirika.mshirika_app.utility.Util.stateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repo: ClientsRepo,
    state: SavedStateHandle
) : MshirikaViewModel() {
    private val _clients = repo.clients.cachedIn(viewModelScope)
    private val _state = state.getLiveData(STATE, DEFAULT)
        .asFlow()
        .stateIn(
            viewModelScope,
            WhileSubscribed(),
            Normal
        ) as MutableStateFlow

    private val _filteredClients = MutableStateFlow<PagingData<Client>>(empty())
    private val searchedClients
        get() = repo.searchedClients

    val clients: StateFlow<PagingData<Client>>
        get() = _state.flatMapLatest {
            when (it) {
                Searching -> _filteredClients
                Normal -> _clients
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = empty()
        )

    fun search(query: String) {
        viewModelScope.launch { repo.search(query) }
    }

    fun state(state: State) {
        _state.value = state
    }

    init {
        viewModelScope.launch {
            handleSearchedClients()
        }
    }

    private suspend fun handleSearchedClients() {
        searchedClients.collectLatest { outcome ->
            outcome.stateHandler(this@ClientsViewModel) {
                data?.let {
                    _filteredClients.value = it
                }
            }
        }
    }

    companion object {
        private const val STATE = "current_state"
        private val DEFAULT: State = Normal
    }
}