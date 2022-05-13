package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.remote.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @Provides
    @ViewModelScoped
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)
}