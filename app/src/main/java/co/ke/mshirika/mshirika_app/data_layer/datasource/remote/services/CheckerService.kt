package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CheckerInbox
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerApprove
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerDelete
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox.CheckerInboxResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint
import retrofit2.Response
import retrofit2.http.*

interface CheckerService {

    @GET(EndPoint.MAKER_CHECKER)
    suspend fun checkerInbox(
        @HeaderMap headers: Map<String, String>
    ): Response<CheckerInboxResponse>

    @POST("${EndPoint.MAKER_CHECKER}/{id}")
    suspend fun approve(
        @HeaderMap headers: Map<String, String>,
        @Path("id") checkerId: Int,
        @Body inbox: CheckerInbox = CheckerInbox(),
        @Query("command") approve: String = "approve"
    ): Response<CheckerApprove>

    @POST("${EndPoint.MAKER_CHECKER}/{id}")
    suspend fun reject(
        @HeaderMap headers: Map<String, String>,
        @Path("id") checkerId: Int,
        @Body inbox: CheckerInbox = CheckerInbox(),
        @Query("command") reject: String = "reject"
    ): Response<CheckerApprove>

    @DELETE("${EndPoint.MAKER_CHECKER}/{id}")
    suspend fun delete(
        @HeaderMap headers: Map<String, String>,
        @Path("id") checkerId: Int
    ): Response<CheckerDelete>
}