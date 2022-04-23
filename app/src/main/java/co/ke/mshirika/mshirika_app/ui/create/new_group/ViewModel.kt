package co.ke.mshirika.mshirika_app.ui.create.new_group

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.request.CreateGroup
import co.ke.mshirika.mshirika_app.data.response.Office
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.repositories.GroupsRepo
import co.ke.mshirika.mshirika_app.ui.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui.util.resourceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: GroupsRepo
) : MshirikaViewModel() {
    private val officeList = mutableListOf<Office>()
    private val _offices = repo.offices

    val offices = flow {
        repo.offices()
        _offices.collectLatest {
            when (it) {
                is Outcome.Error -> errorChannel.send(resourceText(R.string.error_creating_group))
                is Outcome.Success -> it.data?.let { data ->
                    emit(data)
                    officeList.clear()
                    officeList.addAll(data)
                }
                else -> {}
            }
        }
    }.stateIn(viewModelScope, WhileSubscribed(), null)

    suspend fun groupCreated() = repo.groupCreated.map {
        when (it) {
            is Outcome.Success -> it.data
            else -> null
        }
    }.shareIn(viewModelScope, WhileSubscribed()).also {
        it.collectLatest {
            successChannel.send(resourceText(R.string.group_created))
        }
    }

    fun create(group: CreateGroup, officeNameDeco: String) {
        viewModelScope.launch(IO) {
            val id = offices.value
                ?.first { office -> office.nameDecorated == officeNameDeco }?.id?.toString()
                ?.also {
                    repo.create(group.copy(officeId = it))
                }

            if (id == null) errorChannel.send(resourceText(R.string.cannot_find_office))
        }
    }
}