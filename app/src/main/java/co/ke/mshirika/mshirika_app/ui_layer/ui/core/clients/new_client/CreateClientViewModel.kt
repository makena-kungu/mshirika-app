package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.EditClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.CANCELLATION_ERROR
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.NETWORK_ERROR
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.Address
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.FamilyMember
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.GeneralData
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.plainText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateClientViewModel @Inject constructor(
    private val repo: ClientsRepo,
    private val createUseCase: CreateClientUseCase,
    private val editUseCase: EditClientUseCase,
    private val state: SavedStateHandle
) : MshirikaViewModel() {
    private var _task = MutableStateFlow<Task>(Task.Create)
    var task = _task.asLiveData()

    private val _data = MutableStateFlow(mutableMapOf<String, Any>())
    private val _familyMembers = MutableStateFlow(
        mutableListOf(
            *(state.get<List<FamilyMember>>(KEY_FAMILY)
                ?.toTypedArray() ?: emptyArray())
        )
    )
    private val _title = Channel<Int>()

    private var _template: ClientTemplate?
        get() = state[KEY_TEMPLATE]
        set(value) {
            state[KEY_TEMPLATE] = value
        }
    private var _generalData: GeneralData?
        get() = state[KEY_GENERAL_DATA]
        set(value) {
            state[KEY_GENERAL_DATA] = value
        }

    private val famMembers: List<FamilyMember> = state[KEY_FAMILY] ?: emptyList()

    init {
        viewModelScope.launch {
            _title.send(R.string.general_data)
        }
    }

    val data get() = _data.asStateFlow()
    val generalData get() = _generalData
    val familyMembers: StateFlow<List<FamilyMember>> get() = _familyMembers.asStateFlow()
    val title get() = _title.receiveAsFlow().asLiveData()
    val clientCreated = flow {
        repo.created.collectLatest { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    emit(true)
                    false
                }
                is Outcome.Loading -> true
                is Outcome.Error -> {
                    when (outcome.msg) {
                        NETWORK_ERROR -> R.string.network_error
                        CANCELLATION_ERROR -> R.string.request_cancelled
                        else -> R.string.error_creating_client
                    }.also {
                        errorChannel.send(resourceText(it))
                    }
                    false
                }
                else -> false
            }.also { loadingChannel.send(it) }
        }
    }

    fun addFamilyMember(fam: FamilyMember) {
        _familyMembers.value += fam
        state[KEY_FAMILY] = _familyMembers.value
        Log.d(TAG, "addFamilyMember: added $fam")
    }


    fun post() {
        launch(IO) {
            val errorText = plainText("Couldn't create client!")
            val template = _template
            val general = _generalData
            if (template == null || general == null) {
                errorChannel.send(errorText)
                return@launch
            }

            val result = createUseCase(
                general = general,
                template = template,
                familyMembers = famMembers
            )
            if (result == null) {
                errorChannel.send(errorText)
            } else {
                val success = resourceText(R.string.client_created_successfully)
                successChannel.send(success)
            }
        }
    }

    fun updateTitle(@StringRes resId: Int) {
        launch {
            _title.send(resId)
        }
    }

    fun saveGeneralData(data: GeneralData, template: EditClientTemplate?) {
        if (data.isEdit) {
            viewModelScope.launch(IO) {
                editUseCase(data, template!!)
            }
            return
        }

        _generalData = data
    }

    fun saveAddress(address: Address) {
        val value = _data.value
        value[KEY_ADDRESS] = address
        _data.value = value
    }

    suspend fun template(): ClientTemplate? {
        if (_template == null) {
            val template = repo.template()
            _template = template
        }
        return _template
    }

    fun cancel() {
        repo.cancel()
    }

    fun task(task: Task) {
        _task.value = task
    }

    suspend fun getEditTemplate(clientId: Int): EditClientTemplate? {
        return repo.editClientTemplate(clientId)
    }

    companion object {
        private const val TAG = "ViewModel"

        private const val KEY_GENERAL_DATA = "GENERAL_DATA"
        private const val KEY_ADDRESS = "ADDRESS"
        private const val KEY_TEMPLATE = "TEMPLATE"
        private const val KEY_FAMILY = "FAMILY"

    }
}

sealed class Task {
    object Edit : Task()
    object Create : Task()
}