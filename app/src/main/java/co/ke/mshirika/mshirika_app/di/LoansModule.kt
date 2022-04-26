package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object LoansModule {

    @Provides
    @ViewModelScoped
    fun provideLoansService(retrofit: Retrofit):LoansService {
        return retrofit.create(LoansService::class.java)
    }
}