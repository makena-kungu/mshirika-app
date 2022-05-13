package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.CANCELLATION_ERROR
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.NETWORK_ERROR
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.Address
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.FamilyMember
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content.GeneralData
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.PageIndicator
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.plainText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateClientViewModel @Inject constructor(
    private val repo: ClientsRepo,
    private val useCase: CreateClientUseCase,
    private val state: SavedStateHandle
) : MshirikaViewModel() {
    private val _data = MutableStateFlow(mutableMapOf<String, Any>())
    private val _list: MutableList<PageIndicator> = mutableListOf()
    private val _indicators = Channel<MutableList<PageIndicator>>()
    private val _familyMembers = MutableStateFlow(mutableListOf<FamilyMember>())
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

    init {
        viewModelScope.launch {
            _indicators.send(_list)
            _title.send(R.string.general_data)
        }
    }

    val data get() = _data.asStateFlow()
    val generalData get() = _generalData
    val familyMembers: StateFlow<List<FamilyMember>> get() = _familyMembers.asStateFlow()
    val indicators get() = _indicators.receiveAsFlow()
    val title get() = _title.receiveAsFlow()
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
    }


    fun post() {
        launch(Dispatchers.IO) {
            val errorText = plainText("Couldn't create client!")
            val template = _template
            val general = _generalData
            if (template == null || general == null) {
                errorChannel.send(errorText)
                return@launch
            }

            val result = useCase(
                general = general,
                template = template,
                familyMembers = familyMembers.value
            )
            if (result == null) {
                errorChannel.send(errorText)
            } else {
                val success = resourceText(R.string.client_created_successfully)
                successChannel.send(success)
            }
        }
    }

    fun updatePage(position: Int) {
        _list.forEachIndexed { index, indicator ->
            _list[index] = indicator.copy(isSelected = index == position)
        }
        launch {
            _indicators.send(_list)
        }
    }

    fun updateTitle(@StringRes resId: Int) {
        launch {
            _title.send(resId)
        }
    }

    fun saveGeneralData(data: GeneralData) {
        _generalData = data
    }

    fun saveAddress(address: Address) {
        val value = _data.value
        value[KEY_ADDRESS] = address
        _data.value = value
    }

    fun size(size: Int) {
        repeat(size) {
            _list += PageIndicator(it, it == 0)
        }
        launch { _indicators.send(_list) }
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

    companion object {
        private const val TAG = "ViewModel"

        const val KEY_GENERAL_DATA = "GENERAL_DATA"
        const val KEY_ADDRESS = "ADDRESS"
        private const val KEY_TEMPLATE = "TEMPLATE"
    }
}