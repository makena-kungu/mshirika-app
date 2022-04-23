package co.ke.mshirika.mshirika_app.remote.services

import co.ke.mshirika.mshirika_app.data.request.CreateCenter
import co.ke.mshirika.mshirika_app.data.response.Center
import co.ke.mshirika.mshirika_app.remote.response.CenterCreatedResponse
import co.ke.mshirika.mshirika_app.remote.response.CenterResponse
import co.ke.mshirika.mshirika_app.remote.response.OfficeResponse
import co.ke.mshirika.mshirika_app.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.remote.utils.EndPoint.Paths.CENTER_ID
import retrofit2.Response
import retrofit2.http.*

interface CentersService {

    @GET(EndPoint.CENTER)
    suspend fun center(
        @HeaderMap headers: Map<String, String>,
        @Path(CENTER_ID) accountID: Int
    ): Response<Center>

    @GET(EndPoint.CENTERS)
    suspend fun centers(
        @HeaderMap headers: Map<String, String>,
        @Query("paged") paged: Boolean = true,
        @Query("offset") page: Int,
        @Query("limit") pageSize: Int
    ): Response<CenterResponse>

    @POST(EndPoint.CENTERS)
    suspend fun create(
        @HeaderMap headers: Map<String, String>,
        center: CreateCenter
    ): Response<CenterCreatedResponse>

    @GET(EndPoint.OFFICES)
    suspend fun offices(
        @HeaderMap headers: Map<String, String>,
        @Query("orderBy") params: Array<String> = arrayOf("id")
    ): Response<OfficeResponse>
}