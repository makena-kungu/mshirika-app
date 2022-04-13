package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.pagingSource.GroupsPagingSource
import co.ke.mshirika.mshirika_app.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class GroupsRepo @Inject constructor(
    private val service: GroupsService,
    private val pagingSource: GroupsPagingSource,
    private val searchService: SearchService,
    private val authKey: String
) {

    private val _searched = MutableSharedFlow<PagingData<Group>>()
    val searched: Flow<PagingData<Group>> get() = _searched.asSharedFlow()

    val groups = Pager(
        config = pagingConfig(),
        pagingSourceFactory = { pagingSource }
    ).flow
    val headers by lazy {
        headers(authKey)
    }

    suspend fun search(query: String) {
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
                    service.group(
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