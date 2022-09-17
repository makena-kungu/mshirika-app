package co.ke.mshirika.mshirika_app.di

import android.content.Context
import androidx.room.Room
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.CacheDatabase
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.CacheDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.cache.daos.LatestUpdateDao
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.SyncDatabase
import co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos.*
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Urls.BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // todo for logging
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder()
            .create()

        /*.addSerializationExclusionStrategy(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes?): Boolean {
                    return f?.getAnnotation(Exclude::class.java) != null
                }

                override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                    return false
                }
            })*/

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideCacheDatabase(@ApplicationContext context: Context): CacheDatabase {
        return Room.databaseBuilder(
            context,
            CacheDatabase::class.java,
            "cache-database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCacheDao(db: CacheDatabase): CacheDao {
        return db.cacheDao()
    }

    @Provides
    fun provideLatestUpdateDao(db: CacheDatabase): LatestUpdateDao {
        return db.latestUpdateDao()
    }

    @Provides
    @Singleton
    fun provideSyncDatabase(@ApplicationContext context: Context): SyncDatabase {
        return Room.databaseBuilder(
            context,
            SyncDatabase::class.java,
            "sync-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideClientDao(db: SyncDatabase): ClientDao {
        return db.clientDao()
    }

    @Provides
    @Singleton
    fun provideCenterDao(db: SyncDatabase): CenterDao {
        return db.centerDao()
    }

    @Provides
    @Singleton
    fun provideGroupDao(db: SyncDatabase): GroupDao {
        return db.groupDao()
    }

    @Provides
    @Singleton
    fun provideLoanDao(db: SyncDatabase): LoansDao {
        return db.loanDao()
    }

    @Provides
    @Singleton
    fun provideSearchDao(db: SyncDatabase): SearchDao {
        return db.searchDao()
    }
}