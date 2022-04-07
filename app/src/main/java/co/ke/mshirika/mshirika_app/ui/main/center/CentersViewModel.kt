package co.ke.mshirika.mshirika_app.ui.main.center

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.repositories.CentersRepo
import co.ke.mshirika.mshirika_app.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui.main.utils.State.NORMAL
import co.ke.mshirika.mshirika_app.ui.main.utils.State.SEARCHING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CentersViewModel @Inject constructor(
    private val repo: CentersRepo,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = stateHandle
        .getLiveData(STATE, DEFAULT)
        .asFlow()
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed()
        ) as MutableSharedFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    fun data(authKey: String): Flow<PagingData<Center>> = _state.flatMapLatest {
        when (it) {
            SEARCHING -> repo.searched
            NORMAL -> repo.groups(authKey)
            else -> {
                emptyFlow()
            }
        }
    }

    fun search(query: String, authKey: String) {
        viewModelScope.launch(IO) {
            repo.search(query, authKey)
        }
    }

    fun state(state: State) {
        viewModelScope.launch {
            _state.collectLatest {
                if (it != state)
                    _state.tryEmit(state)
            }
        }
    }

    companion object {
        const val STATE = "thecurrentstate"
        val DEFAULT = NORMAL
    }
}