package co.ke.mshirika.mshirika_app.data_layer.remote_mediators

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.CacheDatabase
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.TableLatestUpdated.Companion.PRESTAMOS_TABLE
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.LatestUpdateDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.RemoteMediators.execute
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.RemoteMediators.initialise
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class LoansRemoteMediator @Inject constructor(
    private val service: LoansService,
    private val store: PreferencesStoreRepository,
    private val dao: CacheDao,
    private val cacheDb: CacheDatabase,
    private val latestUpdateDao: LatestUpdateDao
) : RemoteMediator<Int, ConservativeLoanAccount>() {
    private val prestamo = PRESTAMOS_TABLE

    init {
        init()
    }

    fun init() {
        Log.d(TAG, "Clients Remote Mediator: Invoked")
    }

    override suspend fun initialize(): InitializeAction {
        return initialise {
            latestUpdateDao.get(prestamo)
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ConservativeLoanAccount>
    ): MediatorResult {
        Log.d(TAG, "load: initialized")
        return execute(
            tableName = prestamo,
            dao = latestUpdateDao,
            database = cacheDb,
            store = store,
            request = { service.loans(headers = it) },
            actionWithDB = { loans ->
                withTransaction {
                    if (loadType == REFRESH) dao.clearLoans()

                    Log.d(TAG, "load: inserting ${loans.size} loans")

                    val list = mutableListOf<Long>()
                    loans.forEachIndexed { index, loan ->
                        Log.d(TAG, "load: inserting $index")
                        list += dao.insert(loan)
                    }
                    Log.d(TAG, "load: ${list.joinToString()}")
                }
            }
        )
    }

    companion object {
        private const val TAG = "ClientsRemoteMediator"
    }
}