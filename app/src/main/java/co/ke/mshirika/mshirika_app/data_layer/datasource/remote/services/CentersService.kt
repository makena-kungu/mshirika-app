package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateCenter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.Center
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.CenterCreatedResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.OfficeResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint.Paths.CENTER_ID
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Feedback
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
        @Query("paged") paged: Boolean = false
    ): Response<Feedback<Center>>

    @POST(EndPoint.CENTERS)
    suspend fun create(
        @HeaderMap headers: Map<String, String>,
        @Body center: CreateCenter
    ): Response<CenterCreatedResponse>

    @GET(EndPoint.OFFICES)
    suspend fun offices(
        @HeaderMap headers: Map<String, String>,
        @Query("orderBy") params: Array<String> = arrayOf("id")
    ): Response<OfficeResponse>
}