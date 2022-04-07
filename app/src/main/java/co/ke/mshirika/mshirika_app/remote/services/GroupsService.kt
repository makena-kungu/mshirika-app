package co.ke.mshirika.mshirika_app.remote.services

import co.ke.mshirika.mshirika_app.data.response.Group
import co.ke.mshirika.mshirika_app.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.remote.response.GroupResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupsService {

    @GET(EndPoint.GROUPS)
    suspend fun groups(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = true,
        @Query("offset") page: Int,
        @Query("limit") pageSize: Int
    ): Response<GroupResponse>

    @GET("${EndPoint.GROUPS}/{id}")
    suspend fun group(
        @HeaderMap headers: Map<String, String>,
        @Path("id") accountID: Int
    ): Response<Group>
}