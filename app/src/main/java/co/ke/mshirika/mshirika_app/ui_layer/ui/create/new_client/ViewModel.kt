package co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.templates.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.CANCELLATION_ERROR
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.NETWORK_ERROR
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.PageIndicator
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.content.Address
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.content.FamilyMember
import co.ke.mshirika.mshirika_app.ui_layer.ui.create.new_client.content.GeneralData
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.age
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.plainText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ViewModel"

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: ClientsRepo
) : MshirikaViewModel() {
    private val _data = MutableStateFlow(mutableMapOf<String, Any>())
    private val _list = mutableListOf(
        PageIndicator(0, true),
        PageIndicator(1),
        PageIndicator(2),
        PageIndicator(3),
    )
    private val _indicators = Channel<MutableList<PageIndicator>>()
    private val _familyMembers = MutableStateFlow(mutableListOf<FamilyMember>())
    private val _title = Channel<Int>()

    init {
        viewModelScope.launch {
            _indicators.send(_list)
            _title.send(R.string.general_data)
        }
    }

    val data get() = _data.asStateFlow()
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
        viewModelScope.launch(Dispatchers.IO) {
            val outcome = template()
            val errorText = plainText("Couldn't create client!")
            if (outcome !is Outcome.Success) {
                errorChannel.send(errorText)
                return@launch
            }
            val template = outcome.data
            if (template == null) {
                errorChannel.send(errorText)
                return@launch
            }
            template.run {
                (data.value[KEY_GENERAL_DATA] as GeneralData).run {
                    finalPost()
                }
            }
        }
    }

    context (ClientTemplate, GeneralData) private suspend fun finalPost() {

        val savingsProductId = savingProductOptions.first {
            "savings|shares".toRegex() in it.name
        }.id

        if (KEY_GENERAL_DATA !in data.value) {
            val errorText = plainText("Couldn't create client!")
            errorChannel.send(errorText)
            return
        }
        with(data.value[KEY_GENERAL_DATA] as GeneralData) {
            val classification = clientClassificationOptions.first { c ->
                c.name == clientClassification
            }.id
            val type = clientTypeOptions.first { t -> t.name == clientType }.id
            val genderId = genderOptions.first {
                it.name == if (gender) "Male" else "Female"
            }.id
            // TODO: Check if this option should be allowed
            val legalsFormId = clientLegalFormOptions.first().id
            val famMembers = familyMembers.value.map {
                val names = it.name.split(" ".toRegex())

                CreateClient.FamilyMember(
                    firstName = names.first(),
                    lastName = names.last(),
                    age = it.dob.age.toString(),
                    relationshipId = it.relationship,
                    genderId = it.gender,
                    maritalStatusId = it.maritalStatus,
                    dateOfBirth = it.dob.mshirikaDate
                )
            }

            val client = CreateClient(
                officeId = officeOptions.first().id,
                firstname = firstName,
                middlename = middleName,
                lastname = lastName,
                externalId = memNo,
                activationDate = activationDate.mshirikaDate,
                submittedOnDate = submitDate.mshirikaDate,
                savingsProductId = savingsProductId,
                genderId = genderId,
                legalFormId = legalsFormId,
                isStaff = isStaff,
                familyMembers = famMembers,
                clientClassificationId = classification,
                clientTypeId = type,
                dateOfBirth = dob.mshirikaDate
            )
            repo.createClient(client)
        }
    }

    fun updatePage(position: Int) {
        _list.forEachIndexed { index, indicator ->
            _list[index] = indicator.copy(isSelected = index == position)
        }
        viewModelScope.launch {
            _indicators.send(_list)
        }
    }

    fun updateTitle(@StringRes resId: Int) {
        viewModelScope.launch {
            _title.send(resId)
        }
    }

    fun saveGeneralData(data: GeneralData) {
        val value = _data.value
        value[KEY_GENERAL_DATA] = data
        _data.value = value
    }

    fun saveAddress(address: Address) {
        val value = _data.value
        value[KEY_ADDRESS] = address
        _data.value = value
    }

    suspend fun template(): Outcome<ClientTemplate> = repo.template()

    fun cancel() {
        repo.cancel()
    }

    companion object {
        const val KEY_GENERAL_DATA = "GENERAL_DATA"
        const val KEY_ADDRESS = "ADDRESS"
    }
}