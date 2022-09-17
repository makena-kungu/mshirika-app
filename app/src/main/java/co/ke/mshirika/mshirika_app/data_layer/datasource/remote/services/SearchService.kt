package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.SearchResponse
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Feedback
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

    @GET(EndPoint.CLIENTS)
    suspend fun searchClient(
        @HeaderMap headers: Map<String, String>,
        @Query("orphansOnly") orphansOnly: Boolean = false,
        @Query("sortOrder") sortOrder: String = "ASC",
        @Query("orderBy") orderBy: String = "displayName",
        @Query("sqlSearch") sqlSearch: String,
        @Query("paged") paged: Boolean = false,
        @Query("offset") page: Int? = null,
        @Query("limit") perPage: Int? = null
    ): Response<Feedback<Cliente>>

    @GET(EndPoint.CLIENTS)
    suspend fun searchClients(
        @HeaderMap headers: Map<String, String>,
        @Query("orphansOnly") orphansOnly: Boolean = false,
        @Query("sortOrder") sortOrder: String = "ASC",
        @Query("orderBy") orderBy: String = "displayName",
        @Query("sqlSearch") sqlSearch: String,
        @Query("paged") paged: Boolean = true,
        @Query("offset") page: Int,
        @Query("limit") perPage: Int
    ): Response<Feedback<Cliente>>

    companion object {
        val CLIENTS = arrayOf("clients")
        val GROUPS = arrayOf("groups")
        val CENTERS = arrayOf("centers")
        val LOANS = arrayOf("LOANS")
        val ALL = CLIENTS + GROUPS + CENTERS + LOANS

        @JvmStatic
        fun clients(query: String): String {
            return buildQuery(
                query,
                "c.id",
                "c.external_id",
                "display_name",
                "c.mobile_no"
            )
        }

        private fun buildQuery(query: String, vararg fields: String): String {
            val stringBuilder = StringBuilder()
            val like = " LIKE "
            val q = query.encapsulate
            fields.forEachIndexed { index, field ->
                stringBuilder.append(field)
                stringBuilder.append(like)
                stringBuilder.append(q)

                if (index < fields.lastIndex) {
                    stringBuilder.append(" OR ")
                }
            }
            return stringBuilder.toString()
        }

        private val String.encapsulate: String get() = "'%$this%'"

        const val sampleQuery =
            "c.id LIKE '%254715417011%' OR c.external_id LIKE '%254715417011%' OR display_name LIKE '%254715417011%' OR c.mobile_no LIKE '%254715417011%'"
    }
}