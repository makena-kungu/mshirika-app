package co.ke.mshirika.mshirika_app.data_layer.remote.services

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.CreateGroup
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Group
import co.ke.mshirika.mshirika_app.data_layer.remote.response.GroupCreatedResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.response.GroupResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.response.OfficeResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint.Paths.GROUP_ID
import retrofit2.Response
import retrofit2.http.*

interface GroupsService {
    @POST(EndPoint.GROUPS)
    fun create(
        @HeaderMap headers: Map<String, String>,
        group: CreateGroup
    ): Response<GroupCreatedResponse>

    @GET(EndPoint.GROUPS)
    suspend fun groups(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = true,
        @Query("offset") page: Int,
        @Query("limit") pageSize: Int
    ): Response<GroupResponse>

    @GET(EndPoint.GROUP)
    suspend fun group(
        @HeaderMap headers: Map<String, String>,
        @Path(GROUP_ID) accountID: Int
    ): Response<Group>

    @GET(EndPoint.OFFICES)
    suspend fun offices(
        @HeaderMap headers: Map<String, String>,
        @Query("orderBy") params: Array<String> = arrayOf("id")
    ): Response<OfficeResponse>
}