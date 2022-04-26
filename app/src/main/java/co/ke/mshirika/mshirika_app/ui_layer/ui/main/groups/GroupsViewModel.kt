@file:OptIn(ExperimentalCoroutinesApi::class)

package co.ke.mshirika.mshirika_app.ui_layer.ui.main.groups

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.data_layer.repositories.GroupsRepo
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State.Normal
import co.ke.mshirika.mshirika_app.ui_layer.ui.main.utils.State.Searching
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val repo: GroupsRepo,
    private val prefRepo: PreferencesStoreRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(
        value = state.getLiveData(
            STATE,
            DEFAULT
        ).value ?: DEFAULT
    )

    val data: Flow<PagingData<Group>>
        get() = _state.flatMapLatest {
            when (it) {
                Searching -> repo.searched
                Normal -> repo.groups
            }
        }

    fun search(query: String) {
        viewModelScope.launch(IO) {
            val headers = prefRepo.authKey().headers
            repo.search(headers, query)
        }
    }

    fun state(state: State = Normal) {
        _state.value = state
    }

    companion object {
        const val STATE = "thecurrentstate"
        val DEFAULT: State = Normal
    }
}