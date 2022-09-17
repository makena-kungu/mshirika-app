package co.ke.mshirika.mshirika_app.data_layer.remote_mediators

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.CacheDatabase
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.TableLatestUpdated.Companion.CENTROS_TABLE
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.LatestUpdateDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.Center
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.CentersService
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.RemoteMediators.execute
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.RemoteMediators.initialise
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CentersRemoteMediator @Inject constructor(
    private val service: CentersService,
    private val store: PreferencesStoreRepository,
    private val dao: CacheDao,
    private val cacheDb: CacheDatabase,
    private val latestUpdateDao: LatestUpdateDao
) : RemoteMediator<Int, Center>() {
    private val centros = CENTROS_TABLE

    init {
        init()
    }

    fun init() {
        Log.d(TAG, "Centers Remote Mediator: Invoked")
    }

    override suspend fun initialize(): InitializeAction {
        return initialise {
            latestUpdateDao.get(centros)
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Center>): MediatorResult {
        Log.d(TAG, "load: initialized")
        return execute(
            tableName = centros,
            dao = latestUpdateDao,
            database = cacheDb,
            store = store,
            request = { headers ->
                service.centers(
                    headers = headers
                )
            }
        ) { centers ->
            withTransaction {
                if (loadType == REFRESH) {
                    dao.clearCenters()
                }
                dao.insert(*centers)
            }
        }
    }

    companion object {
        private const val TAG = "ClientsRemoteMediator"
    }
}