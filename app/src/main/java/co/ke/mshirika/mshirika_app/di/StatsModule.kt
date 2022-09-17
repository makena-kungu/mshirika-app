package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.StatsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object StatsModule {

    @Provides
    fun providesStatsService(retrofit: Retrofit): StatsService {
        return retrofit.create(StatsService::class.java)
    }
}