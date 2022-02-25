package co.ke.mshirika.mshirika_app.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import co.ke.mshirika.mshirika_app.data.response.Client
import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.utility.mld
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchService: SearchService,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val _centers = MutableSharedFlow<Center>()
    private val _clients = MutableSharedFlow<Client>()
    private val _groups = MutableSharedFlow<Group>()
    private val _loans = MutableSharedFlow<LoanAccount>()

    val centers = _centers.asSharedFlow()
    val clients = _clients.asSharedFlow()
    val groups = _groups.asSharedFlow()
    val loans = _loans.asSharedFlow()

    private val _query = stateHandle.getLiveData<String>(KEY_QUERY)
    private val query = _query.switchMap {
        //query for groups, centers, clients and loan accounts

        mld<Unit>()
    }

    fun setQuery(query: String) {
        _query.value = query
    }

    companion object {
        private const val KEY_QUERY = "QUERY"
    }
}