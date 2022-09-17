package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [ClientsModule.ClientRepoBindModule::class])
@InstallIn(SingletonComponent::class)
object ClientsModule {

    @Provides
    @Singleton
    fun provideClientService(retrofit: Retrofit): ClientsService =
        retrofit.create(ClientsService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface ClientRepoBindModule {

        @Binds
        @Singleton
        fun providesRepo(repoImpl: ClientsRepoImpl): ClientsRepo
    }
}