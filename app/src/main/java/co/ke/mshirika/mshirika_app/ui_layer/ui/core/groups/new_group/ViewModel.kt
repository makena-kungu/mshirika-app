package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups.new_group

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateGroup
import co.ke.mshirika.mshirika_app.data_layer.remote.response.OfficeResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.GroupsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: GroupsRepo,
    private val state: SavedStateHandle
) : MshirikaViewModel() {

    private var offices: OfficeResponse
        get() = state[KEY_OFFICES]!!
        set(value) {
            state[KEY_OFFICES] = value
        }

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
            offices.first { office -> office.nameDecorated == officeNameDeco }
                .id.toString()
                .also { repo.create(group.copy(officeId = it)) }
        }
    }

    suspend fun offices() = withContext(IO) {
        offices = repo.offices() ?: return@withContext null
        offices
    }

    companion object {
        private const val TAG = "ViewModel"
        private const val KEY_OFFICES = "OFFICES"
    }
}