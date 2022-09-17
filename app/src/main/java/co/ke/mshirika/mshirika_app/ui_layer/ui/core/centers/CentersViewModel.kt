@file:OptIn(ExperimentalCoroutinesApi::class)

package co.ke.mshirika.mshirika_app.ui_layer.ui.core.centers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.Center
import co.ke.mshirika.mshirika_app.data_layer.repositories.CentersRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
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
class CentersViewModel @Inject constructor(
    private val repo: CentersRepo,
    private val prefRepo: PreferencesStoreRepository,
    stateHandle: SavedStateHandle
) : MshirikaViewModel() {

    private val _state = MutableStateFlow(
        value = stateHandle.getLiveData(
            STATE,
            DEFAULT
        ).value ?: DEFAULT
    )

    val data: Flow<PagingData<Center>> = prefRepo.isOffline.flatMapLatest {
        when (it) {
            true -> repo.centros
            else -> repo.centers
        }
    }.cachedIn(viewModelScope)

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
        const val STATE = "the_current_state"
        val DEFAULT: State = Normal
    }
}