package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.CentersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CentersModule {

    @Provides
    @Singleton
    fun provideCentersService(retrofit: Retrofit): CentersService {
        return retrofit.create(CentersService::class.java)
    }
}