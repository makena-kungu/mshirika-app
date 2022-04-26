package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.remote.services.CentersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object CentersModule {

    @Provides
    @ViewModelScoped
    fun provideCentersService(retrofit: Retrofit): CentersService {
        return retrofit.create(CentersService::class.java)
    }
}