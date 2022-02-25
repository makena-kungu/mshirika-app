package co.ke.mshirika.mshirika_app.ui.main.client.viewModels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui.main.utils.State.NORMAL
import co.ke.mshirika.mshirika_app.ui.main.utils.State.SEARCHING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repo: ClientsRepo,
    private val authKey: Flow<String>,
    state: SavedStateHandle
) : ViewModel() {
    private val _headers = mutableMapOf("Fineract-Platform-TenantId" to "default")

    private val _clients = repo.clients(_headers["Authorization"]!!)
    private val _state = state.getLiveData(STATE, DEFAULT)
    private val _filteredClients = repo.searched

    val clients: Flow<PagingData<Client>> = _state.switchMap {
        when (it) {
            SEARCHING -> _filteredClients
            NORMAL -> _clients
            else -> flowOf(PagingData.empty()) //when it is null
        }.cachedIn(viewModelScope).asLiveData()
    }.asFlow()

    fun search(query: String) {
        viewModelScope.launch { repo.search(query, _headers) }
    }

    fun state(state: State) {
        _state.value = state
    }

    init {
        viewModelScope.launch {
            authKey.collectLatest {
                _headers["Authorization"] = it
            }
        }
    }

    companion object {
        private const val STATE = "current_state"
        private val DEFAULT = NORMAL
    }
}