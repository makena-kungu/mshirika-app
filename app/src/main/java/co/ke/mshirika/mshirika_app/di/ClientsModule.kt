package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.remote.services.ClientsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientsModule {

    @Provides
    @Singleton
    fun provideClientService(retrofit: Retrofit): ClientsService =
        retrofit.create(ClientsService::class.java)
}