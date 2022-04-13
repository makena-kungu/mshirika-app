package co.ke.mshirika.mshirika_app.pagingSource

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.remote.utils.Feedback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


object Util {

    const val STARTING_PAGING_INDEX = 0
    private val clientsService: ClientsService? = null

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

    fun pagingConfig(
        pageSize: Int = 20,
        maxSize: Int = 60,
        enablePlaceholders: Boolean = true
    ) = PagingConfig(pageSize, maxSize, enablePlaceholders)

    val <Value : Any> PagingState<Int, Value>.refreshKey: Int?
        get() = anchorPosition?.let {
            closestPageToPosition(it)?.prevKey?.plus(1)
                ?: closestPageToPosition(it)?.nextKey?.minus(1)
        }
}