package co.ke.mshirika.mshirika_app.ui.loans

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.LoanAccount
import co.ke.mshirika.mshirika_app.pagingSource.Util.headers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoansViewModel @Inject constructor(
    private val authKey: String,
    private val repo: LoansRepo
) : ViewModel() {

    private val headers: Map<String, String> by lazy {
        headers(authKey)
    }

    val loans: Flow<PagingData<LoanAccount>> get() = TODO()
}