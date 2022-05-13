package co.ke.mshirika.mshirika_app.ui_layer.ui.search

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.core.loan.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.GetsRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.SearchRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepo,
    private val store: PreferencesStoreRepository
) : MshirikaViewModel(),GetsRepaymentSchedule {

    val authKey = store.authKey

    suspend fun authKey(): String {
        return store.authKey()
    }

    fun clientes(query: String) = repo.clientes(query)
        .cachedIn(this)

    fun grupos(query: String) = repo.grupos(query)
        .asLiveData()
        .cachedIn(this)

    fun prestamos(query: String) = repo.prestamos(query)
        .asLiveData()
        .cachedIn(this)
    
    @JvmOverloads
    fun load(isLoading:Boolean = true) {
        viewModelScope.launch {
            Log.d(TAG, "load: sent")
            loadingChannel.send(isLoading)
        }
    }

    override suspend fun repaymentSchedule(loanId: Int): LoanRepaymentSchedule?  = withContext(IO){
        repo.repaymentSchedule(loanId)
    }

    fun stopLoading() {
        load(isLoading = false)
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}