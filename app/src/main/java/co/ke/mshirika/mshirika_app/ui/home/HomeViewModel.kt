package co.ke.mshirika.mshirika_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.ui.home.HomeFragment.State
import co.ke.mshirika.mshirika_app.ui.home.HomeFragment.State.NORMAL
import co.ke.mshirika.mshirika_app.ui.home.HomeFragment.State.SEARCHING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repo: ClientsRepo
) : ViewModel() {

    private val _state = MutableStateFlow(NORMAL)

    val clients =
        repo.clients().cachedIn(viewModelScope)
    val state: Flow<State> = _state.asStateFlow()

    fun changeState() =
        when (_state.value) {
            SEARCHING -> NORMAL
            NORMAL -> SEARCHING
        }.let {
            changeState(it)
        }

    fun changeState(state: State) {
        viewModelScope.launch {
            _state.emit(state)
        }
    }

}