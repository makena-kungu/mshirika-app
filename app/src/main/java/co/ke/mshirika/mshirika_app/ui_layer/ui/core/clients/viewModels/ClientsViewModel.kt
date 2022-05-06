package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingData.Companion.empty
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State.Normal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "ClientsViewModel"

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repo: ClientsRepo,
    private val prefRepo: PreferencesStoreRepository,
    state: SavedStateHandle
) : MshirikaViewModel() {

    private val _filteredClients = Channel<PagingData<Client>>()
    private val _state = MutableStateFlow(state.getLiveData(STATE, DEFAULT).value ?: DEFAULT)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val expresar = channelFlow {
        _state.collectLatest {
            send(it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _clientes = expresar.flatMapLatest {
        when (it) {
            Normal -> repo.clients
            else -> _filteredClients.receiveAsFlow()
        }
    }.cachedIn(viewModelScope)
    val clientes get() = _clientes.stateIn(viewModelScope, WhileSubscribed(), empty())

    fun state(state: State = Normal) {
        _state.value = state
        Log.d(TAG, "state: updated state")
    }

    fun search(query: String) {
        viewModelScope.launch(IO) {
            loadingChannel.send(true)
            val search = repo.search(query)?.pageItems ?: emptyList()
            val list = PagingData.from(search)
            _filteredClients.send(list)
            loadingChannel.send(false)
        }
    }

    suspend fun authKey(): String = withContext(IO) {
        prefRepo.authKey()
    }

    val authKey: Flow<String?> get() = prefRepo.authKey

    companion object {
        private const val STATE = "current_state"
        private val DEFAULT: State = Normal
    }
}