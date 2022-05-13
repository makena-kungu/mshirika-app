package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.viewModels

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.client.Client
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State.Normal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repo: ClientsRepo,
    private val prefRepo: PreferencesStoreRepository
) : MshirikaViewModel() {

    private val _filteredClients = Channel<PagingData<Client>>()
    private val expresar = Channel<State>()

    @OptIn(FlowPreview::class)
    val clientes = expresar.receiveAsFlow().flatMapMerge {
        Log.d(TAG, "clients switched flow ${it is State.Searching}")
        when (it) {
            Normal -> repo.clients
            else -> repo.searchedClients //_filteredClients.receiveAsFlow()
        }
    }.cachedIn(viewModelScope)

    fun state(state: State = Normal) {
        viewModelScope.launch {
            expresar.send(state)
            Log.d(TAG, "state: updated state")
        }
    }

    private fun searching() {
        state(State.Searching)
    }

    fun normal() {
        state(Normal)
    }

    fun search(query: String) {
        Log.d(TAG, "search: invoked")
        searching()
    }

    suspend fun authKey(): String = withContext(IO) {
        prefRepo.authKey()
    }

    val authKey: Flow<String?> get() = prefRepo.authKey


    init {
        viewModelScope.launch {
            expresar.send(DEFAULT)
        }
    }

    companion object {
        private val DEFAULT: State = Normal
        private const val TAG = "ClientsViewModel"
    }
}
