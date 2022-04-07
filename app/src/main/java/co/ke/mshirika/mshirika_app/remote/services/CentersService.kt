package co.ke.mshirika.mshirika_app.remote.services

import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.remote.response.CenterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

interface CentersService {

    @GET(EndPoint.CENTERS)
    suspend fun centers(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = true,
        @Query("offset") page: Int,
        @Query("limit") pageSize: Int
    ): Response<CenterResponse>

    @GET("${EndPoint.CENTERS}/{id}")
    suspend fun center(
        @HeaderMap headers: Map<String, String>,
        @Path("id") accountID: Int
    ): Response<Center>
}