package co.ke.mshirika.mshirika_app.ui_layer.ui.home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.TransactionsWorker
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.dashboard.Statistics
import co.ke.mshirika.mshirika_app.data_layer.repositories.GlobalRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.StatsRepo
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.date
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: StatsRepo,
    store:PreferencesStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    private val context: Context get() = getApplication<Application>()

    private val _status = MutableStateFlow<Status>(Status.Hidden)
    private val _timeValue = MutableStateFlow<TimeValue>(TimeValue.Week)
    val status get() = _status.asStateFlow()

    val clientsDataset = _timeValue.mapLatest {
        withContext(IO) {
            val result = when (it) {
                TimeValue.Day -> getClientsStatsDay()
                TimeValue.Week -> getClientsStatsWeek()
                TimeValue.Month -> getClientsStatsMonth()
            }

            result?.let { stats -> it to stats }
        }
    }.asLiveData()

    val loansDataset = _timeValue.mapLatest {
        withContext(IO) {
            val results = when (it) {
                TimeValue.Day -> getLoansStatsDay()
                TimeValue.Week -> getLoansStatsWeek()
                TimeValue.Month -> getLoansStatsMonth()
            }

            results?.let { statistics -> it to statistics }
        }
    }.asLiveData()

    /* todo val savings = _timeValue.combine(repo.transactions) { time, transactions ->
        val range = time.field.dates
        transactions.filter {
            it.date.date in range
        }.takeIf {
            it.isNotEmpty()
        }?.map {
            it.amount
        }?.reduce { acc, amount ->
            acc + amount
        } ?: run {
            transactions()
            .0
        }
    }.asLiveData()*/

    private val Int.dates: LongRange
        get() {
            val calendar = Calendar.getInstance()
            val min = calendar.getActualMinimum(this)
            val max = calendar.getActualMinimum(this)

            calendar[this] = min
            val first = calendar.timeInMillis
            calendar[this] = max
            val last = calendar.timeInMillis
            return first..last
        }

    val isOffline = store.isOffline.asLiveData()

    private fun transactions() {
        viewModelScope.launch(IO) {
            val latestUpdated = repo.transactionsLatestUpdated() ?: return@launch work()
            val diff = abs(DateUtil.now.milliseconds.inWholeMinutes - latestUpdated.time.milliseconds.inWholeMinutes)
            if (diff < 30L) return@launch
            repo.clearTransactions()
            work()
        }
    }

    private fun work() {
        /*val workManager = WorkManager.getInstance(context)
        workManager.enqueue(OneTimeWorkRequestBuilder<TransactionsWorker>().build())*/
    }

    fun update(status: Status) {
        print(_status.value)
        _status.value = status
        print(_status.value)
    }

    @JvmOverloads
    fun update(timeValue: TimeValue = TimeValue.Week) {
        Log.d(TAG, "update: TimeValue")
        _timeValue.value = timeValue
    }

    private fun print(status: Status) {
        when (status) {
            Status.Hidden -> "Hidden"
            Status.Showing -> "Showing"
        }.also {
            Log.d(TAG, "print: $it")
        }
    }

    private suspend fun getLoansStatsDay(): Statistics? {
        return repo.getLoansStatsDay()
    }

    private suspend fun getLoansStatsWeek(): Statistics? {
        return repo.getLoansStatsWeek()
    }

    private suspend fun getLoansStatsMonth(): Statistics? {
        return repo.getLoansStatsMonth()
    }

    private suspend fun getClientsStatsDay(): Statistics? {
        return repo.getClientsStatsDay()
    }

    private suspend fun getClientsStatsWeek(): Statistics? {
        return repo.getClientsStatsWeek()
    }

    private suspend fun getClientsStatsMonth(): Statistics? {
        return repo.getClientsStatsMonth()
    }
}

private const val TAG = "HomeViewModel"

sealed class Status {
    object Hidden : Status()
    object Showing : Status()
}

sealed class TimeValue(val field: Int) {
    object Day : TimeValue(Calendar.HOUR_OF_DAY)
    object Week : TimeValue(Calendar.DAY_OF_WEEK)
    object Month : TimeValue(Calendar.DAY_OF_MONTH)
}