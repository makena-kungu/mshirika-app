package co.ke.mshirika.mshirika_app.data_layer.repositories

import android.util.Log
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.TableLatestUpdated
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.LatestUpdateDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.dashboard.Statistics
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.StatsService
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.UnpackResponse.respondWithSuccess
import co.ke.mshirika.mshirika_app.data_layer.repositories.Util.headers
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.now
import javax.inject.Inject
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

class StatsRepo @Inject constructor(
    private val service: StatsService,
    private val cService: ClientsService,
    private val store: PreferencesStoreRepository,
    private val dao: CacheDao,
    private val latestUpdateDao: LatestUpdateDao
) {
    val transactions get() = dao.transactions()

    suspend fun getLoansStatsDay(): Statistics? {
        return respondWithSuccess { service.getLoansStatsDay(headers()) }
    }

    suspend fun getLoansStatsWeek(): Statistics? {
        return respondWithSuccess { service.getLoansStatsWeek(headers()) }
    }

    suspend fun getLoansStatsMonth(): Statistics? {
        return respondWithSuccess { service.getLoansStatsMonth(headers()) }
    }

    suspend fun getClientsStatsDay(): Statistics? {
        return respondWithSuccess { service.getClientsStatsDay(headers()) }
    }

    suspend fun getClientsStatsWeek(): Statistics? {
        return respondWithSuccess { service.getClientsStatsWeek(headers()) }
    }

    suspend fun getClientsStatsMonth(): Statistics? {
        return respondWithSuccess { service.getClientsStatsMonth(headers()) }
    }

    suspend fun transactionsLatestUpdated() = latestUpdateDao.get(TableLatestUpdated.TRANSACTIONS_TABLE)
    suspend fun clearTransactions() = dao.clearTransactions()

    suspend fun headers(): Map<String, String> {
        return store.headers()
    }

    companion object {
        private const val TAG = "StatsRepo"
    }
}