package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.dashboard.Statistics
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.EndPoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface StatsService {

    @GET(EndPoint.LOANS_STATS_DAY)
    suspend fun getLoansStatsDay(
        @HeaderMap headers: Map<String, String>
    ): Response<Statistics>

    @GET(EndPoint.LOANS_STATS_WEEK)
    suspend fun getLoansStatsWeek(
        @HeaderMap headers: Map<String, String>
    ): Response<Statistics>

    @GET(EndPoint.LOANS_STATS_MONTH)
    suspend fun getLoansStatsMonth(
        @HeaderMap headers: Map<String, String>
    ): Response<Statistics>

    @GET(EndPoint.CLIENTS_STATS_DAY)
    suspend fun getClientsStatsDay(
        @HeaderMap headers: Map<String, String>
    ): Response<Statistics>

    @GET(EndPoint.CLIENTS_STATS_WEEK)
    suspend fun getClientsStatsWeek(
        @HeaderMap headers: Map<String, String>
    ): Response<Statistics>

    @GET(EndPoint.CLIENTS_STATS_MONTH)
    suspend fun getClientsStatsMonth(
        @HeaderMap headers: Map<String, String>
    ): Response<Statistics>
}