package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.CheckerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object CheckerModule {

    @Provides
    @ViewModelScoped
    fun providesCheckerService(retrofit: Retrofit): CheckerService {
        return retrofit.create(CheckerService::class.java)
    }
}