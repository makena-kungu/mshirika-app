package co.ke.mshirika.mshirika_app.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.repositories.SearchRepo
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import co.ke.mshirika.mshirika_app.utility.mld
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun setQuery(query: String) {
        _query.value = query
    }

    suspend fun authKey(): String {
        return prefRepo.authKey()
    }

    init {
        _query.switchMap { query ->
            viewModelScope.launch(Dispatchers.IO) {
                repo.search(prefRepo.authKey().headers, query)
            }
            mld<Nothing>()
        }
    }

    companion object {
        private const val KEY_QUERY = "QUERY"
    }
}