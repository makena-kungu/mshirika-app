@file:OptIn(ExperimentalCoroutinesApi::class)

package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.repositories.GroupsRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.State.Normal
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
    private val store: PreferencesStoreRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(
        value = state.getLiveData(
            STATE,
            DEFAULT
        ).value ?: DEFAULT
    )

    val data: Flow<PagingData<Grupo>> = store.isOffline.flatMapLatest {
        when (it) {
            true -> repo.grupos
            false -> repo.groups
        }
    }

    fun search(query: String) {
        Log.d(TAG, "search: query = $query")
        viewModelScope.launch(IO) {
            val headers = store.authKey().headers
            repo.search(headers, query)
        }
    }

    fun state(state: State = Normal) {
        _state.value = state
    }

    companion object {
        const val STATE = "thecurrentstate"
        private const val TAG = "GroupsViewModel"
        val DEFAULT: State = Normal
    }
}
