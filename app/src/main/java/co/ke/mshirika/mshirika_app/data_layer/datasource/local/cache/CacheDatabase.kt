package co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.Converter
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.LatestUpdateDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Office
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Transaction
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.Center
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.center.CenterConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.ClientConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.GroupConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.client.ClientTemplateConverter

@Database(
    entities = [
        Center::class,
        Cliente::class,
        TableLatestUpdated::class,
        ClientTemplate::class,
        Grupo::class,
        Office::class,
        ConservativeLoanAccount::class,
        Transaction::class
    ],
    version = 1
)
@TypeConverters(
    ClientConverter::class,
    Converter::class,
    CenterConverter::class,
    GroupConverter::class,
    ClientTemplateConverter::class,
    LoanConverter::class
)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun cacheDao(): CacheDao
    abstract fun latestUpdateDao(): LatestUpdateDao
}