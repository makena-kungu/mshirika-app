package co.ke.mshirika.mshirika_app.data_layer.remote.services

import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Client
import co.ke.mshirika.mshirika_app.data_layer.remote.response.SearchResponse
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.data_layer.remote.utils.Feedback
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

    @GET()
    suspend fun searchClient(
        @HeaderMap headers: Map<String, String>,
        @Query("orphansOnly") orphansOnly: Boolean= false,
        @Query("sortOrder") sortOrder: String = "ASC",
        @Query("orderBy") orderBy: String = "displayName",
        @Query("sqlSearch") sqlSearch: String
    ): Response<Feedback<Client>>

    companion object {
        val CLIENTS = arrayOf("clients")
        val GROUPS = arrayOf("groups")
        val CENTERS = arrayOf("centers")
        val LOANS = arrayOf("LOANS")
        val ALL = CLIENTS + GROUPS + CENTERS + LOANS

        fun clients(query: String) = buildQuery(query, "c.id", "c.external_id", "display_name")

        fun buildQuery(query: String, vararg fields: String):String {
            val stringBuilder = StringBuilder()
            val like = " LIKE "
            val q = query.encapsulate

            println(fields.joinToString())
            fields.forEachIndexed { index, field ->
                stringBuilder.append(field)
                stringBuilder.append(like)
                stringBuilder.append(q)

                println(stringBuilder.toString())

                if (index < fields.lastIndex) {
                    println("($index, true)")
                    stringBuilder.append(" OR ")
                }
            }
            return stringBuilder.toString()
        }

        private val String.encapsulate: String get() = "'%$this%'"

        val sampleQuery = "c.id LIKE '%Bild%' OR c.external_id LIKE '%Bild%' OR display_name LIKE '%Bild%'"
    }
}