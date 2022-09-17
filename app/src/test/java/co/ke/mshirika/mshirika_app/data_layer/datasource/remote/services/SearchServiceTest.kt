package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services

import org.junit.Assert.*
import org.junit.Test

class SearchServiceTest{

    @Test
    fun `testing that the query builder produces the expected value`() {
        val query = "254715417011"
        assertEquals(SearchService.clients(query), SearchService.sampleQuery)
    }
}