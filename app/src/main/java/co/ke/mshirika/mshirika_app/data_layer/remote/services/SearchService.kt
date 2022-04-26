package co.ke.mshirika.mshirika_app.data_layer.remote.services

import co.ke.mshirika.mshirika_app.data_layer.remote.response.SearchResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface SearchService {

    @GET(EndPoint.SEARCH)
    suspend fun search(
        @HeaderMap map: Map<String, String>,
        @Query("exactMatch") exactMatch: Boolean = false,
        @Query("query") query: String,
        @Query("resource") resource: Array<String>
    ): Response<SearchResponse>

    companion object {
        val CLIENTS = arrayOf("clients")
        val GROUPS = arrayOf("groups")
        val CENTERS = arrayOf("centers")
        val LOANS = arrayOf("LOANS")
        val ALL = CLIENTS + GROUPS + CENTERS + LOANS
    }
}