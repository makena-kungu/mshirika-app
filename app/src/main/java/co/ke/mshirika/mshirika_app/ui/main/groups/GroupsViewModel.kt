@file:OptIn(ExperimentalCoroutinesApi::class)

package co.ke.mshirika.mshirika_app.ui.main.groups

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.repositories.GroupsRepo
import co.ke.mshirika.mshirika_app.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Normal
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Searching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val repo: GroupsRepo,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = stateHandle
        .getLiveData(STATE, DEFAULT)
        .asFlow()
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed()
        ) as MutableSharedFlow

    fun data(): Flow<PagingData<Group>> = _state.flatMapLatest {
        when (it) {
            Searching -> repo.searched
            Normal -> repo.groups
        }
    }

    fun search(query: String) {
        viewModelScope.launch(IO) {
            repo.search(query)
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
        val DEFAULT: State = Normal
    }
}