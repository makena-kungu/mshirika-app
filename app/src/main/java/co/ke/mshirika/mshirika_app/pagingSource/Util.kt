package co.ke.mshirika.mshirika_app.pagingSource

import androidx.paging.PagingSource.LoadResult
import co.ke.mshirika.mshirika_app.remote.utils.Feedback
import co.ke.mshirika.mshirika_app.remote.services.ClientsService
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

const val STARTING_PAGING_INDEX = 0

object Util {

    private val clientsService: ClientsService? = null

    suspend fun some() {
        clientsService?.run {
            clients(headers = mapOf(), page = 1, perPage = 0).loadResult(0)
        }
    }

    /**
     * An extension method to help convert a [Response] into a [LoadResult]
     */
    fun <T : Any, V : Feedback<T>> Response<V>.loadResult(
        position: Int
    ): LoadResult<Int, T> {
        return try {
            val list = mutableListOf<T>()
            body()?.also {
                list += it.pageItems
            }

            LoadResult.Page(
                data = list,
                prevKey = if (position == STARTING_PAGING_INDEX) null else position - 1,
                nextKey = if (list.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            //network failures or rather am Interruption of the network during the request
            LoadResult.Error(e)
        } catch (e: HttpException) {
            //for any non 200 Http status code
            LoadResult.Error(e)
        }
    }

    fun headers(authKey: String) = mapOf(
        "Fineract-Platform-TenantId" to "default",
        "Authorization" to authKey
    )
}