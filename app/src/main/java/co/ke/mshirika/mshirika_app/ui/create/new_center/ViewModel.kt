package co.ke.mshirika.mshirika_app.ui.create.new_center

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.request.CreateCenter
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.repositories.CentersRepo
import co.ke.mshirika.mshirika_app.ui.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui.util.resourceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: CentersRepo
) : MshirikaViewModel() {

    private val _offices = repo.offices
    private val _created = viewModelScope.launch(IO) {
        repo.created.collectLatest {
            when (it) {
                is Outcome.Success -> {
                    successChannel.send(resourceText(R.string.center_created))
                }
                is Outcome.Error -> {
                    errorChannel.send(resourceText(R.string.error_creating_center))
                }
                else -> {}
            }
        }
    }

    val offices = flow {
        _offices.collectLatest {
            when (it) {
                is Outcome.Success -> {
                    it.data?.let { data -> emit(data) }
                }
                else -> {}
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun create(center: CreateCenter, office: String) {
        viewModelScope.launch {
            offices.value.first { it.nameDecorated == office }.id.also {
                repo.create(center.copy(officeId = it))
            }
        }
    }

    fun refresh() {
        repo.offices
    }

    init {
        _created
    }
}