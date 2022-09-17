package co.ke.mshirika.mshirika_app.data_layer.remote_mediators

import android.os.Parcelable
import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator.InitializeAction
import androidx.paging.RemoteMediator.MediatorResult
import androidx.paging.RemoteMediator.MediatorResult.Success
import androidx.room.RoomDatabase
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.TableLatestUpdated
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.LatestUpdateDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Feedback
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

@Suppress("UnnecessaryOptInAnnotation")
@OptIn(ExperimentalPagingApi::class)
object RemoteMediators {

    @JvmStatic
    suspend inline fun <reified T : Parcelable, DB : RoomDatabase> response(
        tableName: String,
        dao: LatestUpdateDao,
        database: DB,
        request: () -> Response<Feedback<T>>,
        actionWithDB: DB.(Array<T>) -> Unit
    ): MediatorResult {
        return try {

            val response = request()

            val body = response.body()
                ?: return Success(endOfPaginationReached = true)

            val list = body.pageItems

            dao.insert(
                TableLatestUpdated(
                    id = 0,
                    tableName = tableName,
                    time = System.currentTimeMillis()
                )
            )
            database.actionWithDB(list.toTypedArray())
            Success(endOfPaginationReached = true)
        } catch (e: IOException) {
            //network failures or rather am Interruption of the network during the request
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            //for any non 200 Http status code
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }


    @JvmStatic
    suspend inline fun initialise(
        crossinline latestUpdated: suspend () -> TableLatestUpdated?
    ): InitializeAction {

        val time = latestUpdated()?.time ?: return InitializeAction.LAUNCH_INITIAL_REFRESH
        val howLong = duration(time)
        return when {
            howLong >= 20 -> return InitializeAction.LAUNCH_INITIAL_REFRESH
            else -> InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    @JvmStatic
    fun duration(thenInMillis: Long): Long {
        val now = System.currentTimeMillis()
        val duration = abs(now - thenInMillis)
        return duration.milliseconds.inWholeHours
    }

    @JvmStatic
    suspend inline fun <reified T : Parcelable, Database : RoomDatabase> execute(
        tableName: String,
        dao: LatestUpdateDao,
        database: Database,
        store: PreferencesStoreRepository,
        request: (Map<String, String>) -> Response<Feedback<T>>,
        actionWithDB: Database.( Array<T>) -> Unit
    ): MediatorResult {
        val result = MediatorResult.Error(
            throwable = IllegalArgumentException("Could not find authenticator")
        )
        val headers = withContext(Dispatchers.IO) {
            store.authKey.firstOrNull()?.headers
        } ?: return result

        return response(
            tableName = tableName,
            dao = dao,
            database = database,
            request = { request(headers) },
            actionWithDB = actionWithDB
        )
    }

    const val TAG = "Util"
}






















