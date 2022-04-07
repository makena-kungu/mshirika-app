package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.pagingSource.GroupsPS
import co.ke.mshirika.mshirika_app.pagingSource.Util.headers
import co.ke.mshirika.mshirika_app.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    suspend fun search(query: String, authKey: String) {
        val headers = headers(authKey)
        respond {
            searchService.search(
                map = headers,
                query = query,
                resource = arrayOf("groups")
            )
        }.apply {
            if (this !is Success) return
            val list = mutableListOf<Group>()
            data?.forEach { search ->
                respond {
                    groupsService.group(
                        headers = headers,
                        accountID = search.entityId
                    )
                }.apply {
                    if (this !is Success || data == null) return

                    list += data
                    _searched.tryEmit(PagingData.from(list))
                }
            }
        }
    }

}