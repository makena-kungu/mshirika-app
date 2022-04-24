package co.ke.mshirika.mshirika_app.ui.create.new_client

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.request.CreateClient
import co.ke.mshirika.mshirika_app.data.response.CreateClientTemplate
import co.ke.mshirika.mshirika_app.remote.utils.CANCELLATION_ERROR
import co.ke.mshirika.mshirika_app.remote.utils.NETWORK_ERROR
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.ui.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui.create.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui.create.PageIndicator
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.age
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.mshirikaDate
import co.ke.mshirika.mshirika_app.ui.util.resourceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: ClientsRepo
) : MshirikaViewModel() {
    private val _data = MutableStateFlow(mutableMapOf<String, Any>())
    private val _list = mutableListOf<PageIndicator>()
    private val _indicators = MutableStateFlow(_list)
    private val _familyMembers = MutableStateFlow(mutableListOf<FamilyMember>())

    val data get() = _data.asStateFlow()
    val familyMembers: StateFlow<List<FamilyMember>> get() = _familyMembers.asStateFlow()
    val indicators get() = _indicators.asStateFlow()
    val template = flow {
        repo.template1.collectLatest {
            when (it) {
                is Outcome.Success -> it.data?.let { data -> emit(data) }
                else -> {}
            }
        }
        /*repo.template()
        repo.template.collectLatest { outcome ->
            when (outcome) {
                is Outcome.Error -> errorChannel.send(UIText.PlainText(outcome.msg))
                is Outcome.Success -> outcome.data?.let { emit(it) }
                else -> {}
            }
        }*/
    }.shareIn(viewModelScope, WhileSubscribed())
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

    private fun initList() {
        //generates as list with the first fragment as selected

        FormPagingAdapter.fragments.forEach { (index, _) ->
            _list += PageIndicator(index, index == 0)
        }
        _list.sortBy { it.index }
        _indicators.value = _list
    }

    fun post() {
        viewModelScope.launch(Dispatchers.IO) {
            val template = template.last()
            template.run {
                (data.value[KEY_GENERAL_DATA] as GeneralData).run {
                    finalPost()
                }
            }
        }
    }

    context(CreateClientTemplate, GeneralData) private suspend fun finalPost() {
        val savingsProductId = savingProductOptions.first {
            it.name.contains("savings", ignoreCase = true)
        }.id

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
                val relationshipId = familyMemberOptions.relationshipIdOptions.first { r ->
                    r.name == it.relationship
                }.id
                val famGenderId = genderOptions.first { g ->
                    g.name == if (gender) "Male" else "Female"
                }.id
                val maritalStatusId = familyMemberOptions.maritalStatusIdOptions.first { c ->
                    c.name == it.maritalStatus
                }.id

                CreateClient.FamilyMember(
                    firstName = names.first(),
                    lastName = names.last(),
                    age = it.dob.age.toString(),
                    relationshipId = relationshipId,
                    genderId = famGenderId,
                    maritalStatusId = maritalStatusId,
                    dateOfBirth = it.dob.mshirikaDate
                )
            }

            CreateClient(
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
            ).also {
                repo.createClient(it)
            }
        }
    }

    fun updatePage(position: Int) {
        _list.forEach {
            _list[it.index] = it.copy(isSelected = it.index == position)
        }
        _indicators.value = _list
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

    fun cancel() {
        repo.cancel()
    }

    init {
        initList()
    }

    companion object {
        const val KEY_GENERAL_DATA = "GENERAL_DATA"
        const val KEY_ADDRESS = "ADDRESS"
    }
}