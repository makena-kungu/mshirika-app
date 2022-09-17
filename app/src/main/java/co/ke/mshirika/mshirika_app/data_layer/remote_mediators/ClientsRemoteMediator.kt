package co.ke.mshirika.mshirika_app.data_layer.remote_mediators

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.CacheDatabase
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.TableLatestUpdated.Companion.CLIENTES_TABLE
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.LatestUpdateDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente.Companion.color
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.RemoteMediators.execute
import co.ke.mshirika.mshirika_app.data_layer.remote_mediators.RemoteMediators.initialise
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ClientsRemoteMediator @Inject constructor(
    private val service: ClientsService,
    private val store: PreferencesStoreRepository,
    private val dao: CacheDao,
    private val cacheDb: CacheDatabase,
    private val latestUpdateDao: LatestUpdateDao
) : RemoteMediator<Int, Cliente>() {

    private val clients = CLIENTES_TABLE

    init {
        init()
    }

    fun init() {
        Log.d(TAG, "Clients Remote Mediator: Invoked")
    }

    override suspend fun initialize(): InitializeAction {
        return initialise { latestUpdateDao.get(tableName = clients) }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Cliente>
    ): MediatorResult {

        Log.d(TAG, "load: initialized")
        return execute(
            tableName = clients,
            dao = latestUpdateDao,
            database = cacheDb,
            store = store,
            request = { headers ->
                Log.d(TAG, "load: initiated")
                service.clients(headers = headers)
            },
            actionWithDB = { clients ->
                val array = clients.map {
                    // TODO: remove the comments
                    //val response = service.image(store.headers(), it.id)
                    val hasImage = false //response.isSuccessful
                    it.copy(
                        color = color(),
                        hasImage = hasImage
                    )
                }
                withTransaction {
                    if (loadType == REFRESH) {
                        dao.clearClients()
                    }
                    dao.insert(array)
                }
            }
        )
    }

    companion object {
        private const val TAG = "ClientsRemoteMediator"
    }
}