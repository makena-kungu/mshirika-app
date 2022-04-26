package co.ke.mshirika.mshirika_app.data_layer.remote.services

import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Login
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Staff
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers("Fineract-Platform-TenantId: default")
    @POST(EndPoint.AUTHENTICATION)
    suspend fun login(@Body login: Login): Response<Staff>
}