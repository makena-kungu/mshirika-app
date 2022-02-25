package co.ke.mshirika.mshirika_app.remote.services

import co.ke.mshirika.mshirika_app.data.request.Login
import co.ke.mshirika.mshirika_app.data.response.Staff
import co.ke.mshirika.mshirika_app.remote.EndPoint
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers("Fineract-Platform-TenantId: default")
    @POST(EndPoint.AUTHENTICATION)
    fun login(@Body login: Login): Response<Staff>
}