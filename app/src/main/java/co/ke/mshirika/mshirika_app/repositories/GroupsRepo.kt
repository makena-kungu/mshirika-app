package co.ke.mshirika.mshirika_app.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import co.ke.mshirika.mshirika_app.data.request.CreateGroup
import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.pagingSource.GroupsPagingSource
import co.ke.mshirika.mshirika_app.pagingSource.Util.pagingConfig
import co.ke.mshirika.mshirika_app.remote.response.GroupCreatedResponse
import co.ke.mshirika.mshirika_app.remote.response.OfficeResponse
import co.ke.mshirika.mshirika_app.remote.services.GroupsService
import co.ke.mshirika.mshirika_app.remote.services.SearchService
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.Success
import co.ke.mshirika.mshirika_app.remote.utils.UnpackResponse.respond
import co.ke.mshirika.mshirika_app.remote.utils.empty
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.utility.Util.headers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GroupsRepo @Inject constructor(
    private val service: GroupsService,
    private val searchService: SearchService,
    private val pref: PreferencesStoreRepository
) {

    private val _groupCreated = MutableStateFlow<Outcome<GroupCreatedResponse>>(empty())
    private val _offices = MutableStateFlow<Outcome<OfficeResponse>>(empty())
    private val _searched = MutableStateFlow<PagingData<Group>>(PagingData.empty())

    val groupCreated get() = _groupCreated.asStateFlow()
    val searched get() = _searched.asStateFlow()
    val offices get() = _offices.asStateFlow()

    val groups: Flow<PagingData<Group>>
        get() {
            return flow {
                val headers = pref.authKey().headers
                Pager(
                    config = pagingConfig(),
                    pagingSourceFactory = { GroupsPagingSource(headers, service) }
                ).flow
            }
        }

    suspend fun search(headers: Map<String, String>, query: String) {
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

    suspend fun offices() {
        respond {
            service.offices(headers())
        }.also { _offices.value = it }
    }

    suspend fun create(group: CreateGroup) {
        respond {
            service.create(
                headers(),
                group
            )
        }.also { _groupCreated.value = it }
    }

    private suspend fun headers() = pref.authKey().headers
}