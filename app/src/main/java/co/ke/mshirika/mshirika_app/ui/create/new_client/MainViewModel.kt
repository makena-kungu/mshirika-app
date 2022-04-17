package co.ke.mshirika.mshirika_app.ui.create.new_client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data.request.Client
import co.ke.mshirika.mshirika_app.ui.create.FormPagingAdapter
import co.ke.mshirika.mshirika_app.ui.create.PageIndicator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel : ViewModel() {
    private val _list = mutableListOf<PageIndicator>()
    private val _indicators = MutableStateFlow(_list)

    val indicators get() = _indicators.asStateFlow()

    fun cacheDataFromGeneral() {

    }

    fun postClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            //post the client
        }
    }

    fun submit() {
        TODO("Not yet implemented")
    }

    private fun initList() {
        //generates as list with the first fragment as selected
        FormPagingAdapter.fragments.forEach { (index, _) ->
            _list += PageIndicator(index, index == 0)
        }
        _list.sortBy { it.index }
        _indicators.value = _list
    }

    fun updatePage(position: Int) {
        _list.forEach {
            if (it.index == position && it.isSelected) return
        }

        _list[position] = _list[position].copy(isSelected = false)
        _indicators.value = _list
    }

    init {
        initList()
    }
}