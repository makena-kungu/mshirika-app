package co.ke.mshirika.mshirika_app.ui_layer.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.GetsRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.SearchRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.utility.ld
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepo,
    private val store: PreferencesStoreRepository
) : MshirikaViewModel(), GetsRepaymentSchedule {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asLiveData()

    val authKey = store.authKey

    private val _query = MutableSharedFlow<String>(replay = 1)
    val query: ld<String> = _query.asLiveData()

    private val _clientes = _query.flatMapLatest { consulta ->
        _loading.emit(true)
        withContext(IO) { repo.clientes(consulta) }
    }.onEach {
        _loading.emit(false)
    }.asLiveData().cachedIn(this)
    val clientes: LiveData<PagingData<Cliente>> = _clientes

    private val _grupos = _query.flatMapLatest { consulta ->
        _loading.emit(true)
        repo.grupos(consulta)
    }.onEach {
        _loading.emit(false)
    }
    val grupos: LiveData<PagingData<Grupo>>
        get() = _grupos.asLiveData()

    private val _prestamos = _query.flatMapLatest { consulta ->
        _loading.emit(true)
        repo.prestamos(consulta)
    }.onEach {
        _loading.emit(false)
    }
    val prestamos: LiveData<PagingData<ConservativeLoanAccount>>
        get() = _prestamos.asLiveData()

    suspend fun authKey(): String {
        return store.authKey()
    }

    override suspend fun repaymentSchedule(loanId: Int): LoanRepaymentSchedule? = withContext(IO) {
        repo.repaymentSchedule(loanId)
    }

    fun search(query: String) {
        _query.tryEmit(query)
    }

    init {
        launch {
            loading.asFlow().collectLatest {
                Log.d(TAG, "loading: $it")
            }
        }
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}