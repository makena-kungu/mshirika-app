package co.ke.mshirika.mshirika_app.ui.main.client.viewModels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui.main.utils.State.NORMAL
import co.ke.mshirika.mshirika_app.ui.main.utils.State.SEARCHING
import co.ke.mshirika.mshirika_app.utility.network.Result.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repo: ClientsRepo,
    private val authKey: Flow<String>,
    state: SavedStateHandle
) : ViewModel() {

    //Do recall that the init block is at the bottom
    private val _headers = mutableMapOf("Fineract-Platform-TenantId" to "default")

    private val _clients = repo.clients(_headers["Authorization"]!!).cachedIn(viewModelScope)
    private val _state = state.getLiveData(STATE, DEFAULT)
    private val _filteredClients = MutableStateFlow<PagingData<Client>>(PagingData.empty())
    val searchedClients = repo.searchedClients

    suspend fun getClients(): StateFlow<PagingData<Client>> {
        val clients = _state.switchMap {
            when (it) {
                SEARCHING -> _filteredClients.asStateFlow()
                NORMAL -> _clients
                else -> flowOf(PagingData.empty()) //when it is null
            }.asLiveData()
        }.asFlow()
        return clients.stateIn(viewModelScope)
    }

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

            searchedClients.collectLatest { outcome ->
                if (outcome is Success)
                    outcome.data?.let {
                        _filteredClients.value = it
                    }
            }
        }
    }

    companion object {
        private const val STATE = "current_state"
        private val DEFAULT = NORMAL
    }
}