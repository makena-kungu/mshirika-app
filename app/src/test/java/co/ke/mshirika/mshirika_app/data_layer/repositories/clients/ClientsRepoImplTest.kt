package co.ke.mshirika.mshirika_app.data_layer.repositories.clients

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.CreateClient
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.ClientCreationResponse
import com.google.gson.Gson

class ClientsRepoImplTest {
    suspend fun createClient(client: CreateClient): ClientCreationResponse? {
        val gson = Gson()
        val json = gson.toJson(client)
        println("json = \n$json")
        return null
    }
}