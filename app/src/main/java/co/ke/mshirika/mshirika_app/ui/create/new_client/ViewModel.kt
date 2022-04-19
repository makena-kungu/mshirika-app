package co.ke.mshirika.mshirika_app.ui.create.new_client

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data.request.Client
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.repositories.ClientsRepo
import co.ke.mshirika.mshirika_app.ui.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui.create.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui.create.PageIndicator
import co.ke.mshirika.mshirika_app.ui.util.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    repo: ClientsRepo
) : MshirikaViewModel() {
    private val _list = mutableListOf<PageIndicator>()
    private val _indicators = MutableStateFlow(_list)
    private val _familyMembers = MutableStateFlow(mutableListOf<FamilyMember>())

    private val generaData = mutableMapOf<String, Any>()

    val familyMembers: StateFlow<List<FamilyMember>> get() = _familyMembers.asStateFlow()
    val indicators get() = _indicators.asStateFlow()
    val template = flow {
        repo.template()
        repo.template.collectLatest { outcome ->
            when (outcome) {
                is Outcome.Error -> errorChannel.send(UIText.PlainText(outcome.msg))
                is Outcome.Success -> outcome.data?.let { emit(it) }
                else -> {}
            }
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

    fun postClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            //post the client
        }
    }

    fun submit() {
        TODO("Not yet implemented")
    }

    fun updatePage(position: Int) {
        _list.forEach {
            _list[it.index] = it.copy(isSelected = it.index == position)
        }
        _indicators.value = _list
    }

    fun post() {

    }

    fun saveGeneralDate(vararg pairs: Pair<String, Any>) {
        generaData.putAll(pairs)
    }

    init {
        initList()
    }

    companion object {
        const val KEY_FIRST_NAME = "firstName"
        const val KEY_MIDDLE_NAME = "middleName"
        const val KEY_LAST_NAME = "lastName"
        const val KEY_DOB = "dob"
        const val KEY_MEM_NO = "memNo"
        const val KEY_MOBILE_NO = "mobileNo"
        const val KEY_GENDER_GROUP = "genderGroup"
        const val KEY_IS_STAFF = "isStaff"
        const val KEY_CLIENT_TYPE = "clientType"
        const val KEY_CLIENT_CLASSIFICATION = "clientClassification"
        const val KEY_SUBMIT_DATE = "submitDate"
        const val KEY_NATIONAL_ID = "nationalId"
        const val KEY_EMAIL = "email"
        const val KEY_ACTIVATION_DATE = "activationDate"
    }
}