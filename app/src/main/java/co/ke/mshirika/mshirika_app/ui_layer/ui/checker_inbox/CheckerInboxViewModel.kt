package co.ke.mshirika.mshirika_app.ui_layer.ui.checker_inbox

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerTask
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.CheckerRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.checker_inbox.CheckerInboxViewModel.Companion.Filter.Action
import co.ke.mshirika.mshirika_app.ui_layer.ui.checker_inbox.CheckerInboxViewModel.Companion.Filter.Entity
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.UIText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.plainText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CheckerInboxViewModel @Inject constructor(
    private val repo: CheckerRepo
) : MshirikaViewModel() {

    private val updated = repo.updater.asStateFlow()
    private val _filterTask = MutableStateFlow<MutableSet<Filter>>(mutableSetOf())

    val list = updated.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly
    )

    val success = list.combine(_filterTask) { outcome, filterTask ->
        if (outcome is Outcome.Loading) loadingChannel.send(true)
        if (outcome !is Outcome.Success) return@combine emptyList()
        Log.d(TAG, "Success: ")
        val list = outcome.data ?: emptyList()
        list.sortedBy { it.madeOnDate }.asReversed().filter {
            val conditions = mutableListOf<Boolean>()
            filterTask.forEach { filter ->
                conditions += when (filter) {
                    is Action -> it.actionName.equals(filter.value, true) || filter.value == "All"
                    is Filter.Date -> it.madeOnDate in filter.from..filter.to || filter.value == "None"
                    is Entity -> it.entityName.equals(filter.value, true) || filter.value == "All"
                    Filter.None -> true
                }
            }
            conditions.all { it }
        }
    }.asLiveData()

    private val filters = list.mapLatest { outcome ->
        if (outcome !is Outcome.Success) return@mapLatest null

        val data = outcome.data?.filter { true } ?: return@mapLatest null

        val entities = data.map { it.entityName }.distinct().toTypedArray()
        val entityList = listOf("All", *entities)
        val actions = data.map { it.actionName }.distinct().toTypedArray()
        val actionList = listOf("All", *actions)

        mutableMapOf(
            Entity to entityList,
            Action to actionList
        )
    }.shareIn(viewModelScope, SharingStarted.Eagerly)

    val entities = filters.map {
        it?.get(Entity) ?: emptyList()
    }.asLiveData()

    val actions = filters.map {
        it?.get(Action) ?: emptyList()
    }.asLiveData()

    val loading = list.mapLatest {
        it is Outcome.Loading
    }.asLiveData()

    val error = list.mapLatest {
        if (it !is Outcome.Error) return@mapLatest null

        val msg = it.msg
        val text: UIText = plainText(msg)
        text
    }.asLiveData()

    fun approve(task: CheckerTask) {
        viewModelScope.launch {
            request {
                repo.approve(task)
            }
        }
    }

    fun reject(task: CheckerTask) {
        viewModelScope.launch {
            request {
                repo.reject(task)
            }
        }
    }

    fun delete(task: CheckerTask) {
        viewModelScope.launch {
            request {
                repo.delete(task)
            }
        }
    }

    private suspend inline fun <T> request(request: () -> Outcome<T>) {
        val outcome = request()
        outcome.handle()
    }

    private suspend fun <T> Outcome<T>.handle() {
        when (this) {
            is Outcome.Error -> {
                errorChannel.send(plainText(msg))
            }
            else -> {}
        }
    }

    fun filter(task: Filter): Filter {
        val set = _filterTask.value
        if (set.contains(task)) set.remove(task)
        set += task
        _filterTask.value = set

        return task
    }

    init {
        viewModelScope.launch(IO) { repo.list() }
    }

    companion object {
        private const val TAG = "CheckerInboxViewModel"

        sealed class Filter(val name: String, var value: String? = null) {
            object None : Filter("None")
            object Entity : Filter("Entity")
            object Action : Filter("Action")
            data class Date(
                var from: Long = System.currentTimeMillis(),
                var to: Long = System.currentTimeMillis()
            ) : Filter("Date")
        }
    }
}