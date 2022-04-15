@file:OptIn(ExperimentalCoroutinesApi::class)

package co.ke.mshirika.mshirika_app.ui.main.centers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.repositories.CentersRepo
import co.ke.mshirika.mshirika_app.ui.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Normal
import co.ke.mshirika.mshirika_app.ui.main.utils.State.Searching
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CentersViewModel @Inject constructor(
    private val repo: CentersRepo,
    private val prefRepo: PreferencesStoreRepository,
    stateHandle: SavedStateHandle
) : MshirikaViewModel() {

    private val _state = stateHandle
        .getLiveData(STATE, DEFAULT)
        .asFlow()
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed()
        ) as MutableSharedFlow
    private val searched = repo.searched

    val data: Flow<PagingData<Center>>
        get() = _state.flatMapLatest {
            when (it) {
                Searching -> searched
                Normal ->  repo.centers.stateIn(viewModelScope)
            }
        }

    fun search(query: String) {
        viewModelScope.launch(IO) {
            val headers = prefRepo.authKey().headers
            repo.search(headers, query)
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
        const val STATE = "the_current_state"
        val DEFAULT: State = Normal
    }
}