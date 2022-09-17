package co.ke.mshirika.mshirika_app.data_layer.repositories

import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.CacheDatabase
import javax.inject.Inject

class GlobalRepo @Inject constructor(
    private val db: CacheDatabase
) {

    fun clear() {
        db.clearAllTables()
    }

}