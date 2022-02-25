package co.ke.mshirika.mshirika_app.remote.services

import co.ke.mshirika.mshirika_app.remote.response.SearchResponse
import co.ke.mshirika.mshirika_app.remote.EndPoint
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
    ): Response<List<SearchResponse>>
}