package co.ke.mshirika.mshirika_app.data_layer.pagingSource

import android.os.Parcelable
import android.util.Log
import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Feedback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


object Util {

    const val STARTING_PAGING_INDEX = 0
    private const val TAG = "Util"

    /**
     * An extension method to help convert a [Response] into a [LoadResult]
     */
    fun <T : Parcelable> Response<Feedback<T>>.loadResult(
        position: Int,
        loadSize: Int
    ): LoadResult<Int, T> = try {
        val list = mutableListOf<T>()
        body()?.run { pageItems.forEach { if (it !in list) list += it } }
        val size = list.size
        Log.d(TAG, "loadResult: size = $size")
        Log.d(TAG, "loadResult: load size = $loadSize")
        val hasMore = size == loadSize
        LoadResult.Page(
            data = list,
            prevKey = if (position == STARTING_PAGING_INDEX) null else position - 1,
            nextKey = if (!hasMore) null else position + 1
            // when the list returned has size less than the load size means that the list does not
            // have any other records
            // todo check if the change works.
        )
    } catch (e: IOException) {
        //network failures or rather am Interruption of the network during the request
        LoadResult.Error(e)
    } catch (e: HttpException) {
        //for any non 200 Http status code
        LoadResult.Error(e)
    } catch (e: SocketTimeoutException) {
        val message = "Slow network connectivity!"
        LoadResult.Error(SocketTimeoutException(message))
    }

    fun pagingConfig(
        pageSize: Int = 20,
        maxSize: Int = 30,
        enablePlaceholders: Boolean = true
    ) = PagingConfig(
        pageSize = pageSize,
        maxSize = maxSize,
        prefetchDistance = 5,
        enablePlaceholders = enablePlaceholders
    )

    val <Value : Any> PagingState<Int, Value>.refreshKey: Int?
        get() = anchorPosition?.let { anchorPosition ->
            closestPageToPosition(anchorPosition)
                ?.prevKey
                ?.let { it + 1 }
                ?: closestPageToPosition(anchorPosition)
                    ?.nextKey
                    ?.let { it - 1 }
        }
}