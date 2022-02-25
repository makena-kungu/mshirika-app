package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.pagingSource.GroupsPS
import co.ke.mshirika.mshirika_app.pagingSource.Util.headers
import co.ke.mshirika.mshirika_app.remote.response.GroupResponse
import co.ke.mshirika.mshirika_app.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupsRepo @Inject constructor(
    private val groupsService: GroupsService,
    private val searchService: SearchService
) {

    private val _searched = MutableSharedFlow<PagingData<Group>>()
    val searched: Flow<PagingData<Group>> get() = _searched.asSharedFlow()

    fun groups(authKey: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 60,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                GroupsPS(
                    authKey,
                    groupsService = groupsService
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
            val result: Result<GroupResponse> = when {
                isSuccess -> {
                    val lt = getOrNull()!!.body()?.run {
                        val list = mutableListOf<Group>()
                        //query the groups with the provided accountIds
                        parallelStream().forEach {
                            coroutineScope.launch(Dispatchers.IO) {
                                groupsService.group(
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
                    Result.success(GroupResponse(lt))
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