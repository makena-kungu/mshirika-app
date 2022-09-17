package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateGroup
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.DetailedGroup
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.GroupCreatedResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.OfficeResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.GROUP_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Feedback
import retrofit2.Response
import retrofit2.http.*

interface GroupsService {
    @POST(EndPoint.GROUPS)
    suspend fun create(
        @HeaderMap headers: Map<String, String>,
        @Body group: CreateGroup
    ): Response<GroupCreatedResponse>

    @GET(EndPoint.GROUPS)
    suspend fun groups(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = true,
    ): Response<Feedback<Grupo>>

    @GET(EndPoint.GROUP)
    suspend fun group(
        @HeaderMap headers: Map<String, String>,
        @Path(GROUP_ID) accountID: Int
    ): Response<Grupo>

    @GET(EndPoint.GROUP_DETAILED)
    suspend fun detailedGroup(
        @HeaderMap headers: Map<String, String>,
        @Path(GROUP_ID) accountID: Int
    ): Response<DetailedGroup>

    @GET(EndPoint.OFFICES)
    suspend fun offices(
        @HeaderMap headers: Map<String, String>,
        @Query("orderBy") params: Array<String> = arrayOf("id")
    ): Response<OfficeResponse>
}