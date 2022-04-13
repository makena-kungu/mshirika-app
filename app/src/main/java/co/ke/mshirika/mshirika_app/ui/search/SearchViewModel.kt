package co.ke.mshirika.mshirika_app.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.repositories.SearchRepo
import co.ke.mshirika.mshirika_app.utility.mld
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepo,
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

    var authKey: String? = null

    fun setQuery(query: String) {
        _query.value = query
    }

    init {
        _query.switchMap { query ->
            authKey?.also { authKey ->
                viewModelScope.launch(IO) {
                    repo.search(query)
                }
            }
            mld<Nothing>()
        }
    }

    companion object {
        private const val KEY_QUERY = "QUERY"
    }
}