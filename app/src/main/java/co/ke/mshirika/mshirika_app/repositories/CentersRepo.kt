package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.pagingSource.CentersPS
import co.ke.mshirika.mshirika_app.pagingSource.Util.headers
import co.ke.mshirika.mshirika_app.remote.response.CenterResponse
import co.ke.mshirika.mshirika_app.remote.services.CentersService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CentersRepo @Inject constructor(
    private val centersService: CentersService,
    private val searchService: SearchService
) {

    private val _searched = MutableSharedFlow<PagingData<Center>>()
    val searched: Flow<PagingData<Center>> get() = _searched.asSharedFlow()

    fun groups(authKey: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 60,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CentersPS(
                    authKey = authKey,
                    centersService = centersService
                )
            }
        ).flow

    suspend fun search(query: String, authKey: String, coroutineScope: CoroutineScope) {
        searchService.search(
            map = headers(authKey),
            query = query,
            resource = arrayOf("groups")
        ).let {
            kotlin.runCatching { it }
        }.apply {
            val result: Result<CenterResponse> = when {
                isSuccess -> {
                    val lt = getOrNull()!!.body()?.run {
                        val list = mutableListOf<Center>()
                        //query the groups with the provided accountIds
                        parallelStream().forEach {
                            coroutineScope.launch(Dispatchers.IO) {
                                centersService.center(
                                    headers = headers(authKey),
                                    accountID = it.entityId
                                ).apply {
                                    if (isSuccessful) {
                                        body()?.let { list += it }
                                    }
                                }
                            }
                        }
                        list
                    } ?: emptyList()
                    Result.success(CenterResponse(lt))
                }
                else -> {
                    //create a throwable for why the failure occurred
                    Result.failure(exceptionOrNull()!!)
                }
            }

            when {
                result.isSuccess -> result.getOrNull()
                    ?.also { _searched.tryEmit(PagingData.from(it.pageItems)) }
            }
        }
    }

}