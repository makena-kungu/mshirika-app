package co.ke.mshirika.mshirika_app.data_layer.remote_mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.CacheDatabase
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.TableLatestUpdated.Companion.GRUPOS_TABLE
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.LatestUpdateDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.RemoteMediators.execute
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GroupsRemoteMediator @Inject constructor(
    private val service: GroupsService,
    private val store: PreferencesStoreRepository,
    private val dao: CacheDao,
    private val cacheDb: CacheDatabase,
    private val latestUpdateDao: LatestUpdateDao
) : RemoteMediator<Int, Grupo>() {
    private val grupo = GRUPOS_TABLE

    override suspend fun initialize(): InitializeAction {
        return RemoteMediators.initialise {
            latestUpdateDao.get(grupo)
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Grupo>): MediatorResult {
        return execute(
            tableName = grupo,
            dao = latestUpdateDao,
            database = cacheDb,
            store = store,
            request = { service.groups(headers = it) },
            actionWithDB = { groups ->
                withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        dao.clearGroups()
                    }

                    dao.insert(*groups)
                }
            }
        )
    }
}