package co.ke.mshirika.mshirika_app.ui_layer.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.repositories.SearchRepo
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.FlowUtils.collectLatestNonNull
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.mld
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepo,
    private val prefRepo: PreferencesStoreRepository,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val centers
        get() = repo.centers
    val clients
        get() = repo.clients
    val groups
        get() = repo.groups
    val loans
        get() = repo.loans

    private val _query = stateHandle.getLiveData<String>(KEY_QUERY)
    private val _modifiedQuery = MutableStateFlow(stateHandle.getLiveData<String>(KEY_QUERY).value)

    @OptIn(ExperimentalCoroutinesApi::class)
    val query = channelFlow<Unit> {
        withContext(Dispatchers.IO) {
            _modifiedQuery.collectLatestNonNull {
                repo.search(it)
            }
        }
    }

    fun setQuery(query: String) {
        //_query.value = query
        _modifiedQuery.value = query
    }

    suspend fun authKey(): String {
        return prefRepo.authKey()
    }

    val authKey = prefRepo.authKey

    init {
        _query.switchMap { query ->
            viewModelScope.launch(Dispatchers.IO) {
                repo.search(query)
            }
            mld<Nothing>()
        }
    }

    companion object {
        private const val KEY_QUERY = "QUERY"
    }
}