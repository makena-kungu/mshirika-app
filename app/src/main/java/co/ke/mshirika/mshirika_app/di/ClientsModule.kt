package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.remote.services.ClientsService
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.clients.ClientsRepoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module(includes = [ClientsModule.ClientRepoBindModule::class])
@InstallIn(ViewModelComponent::class)
object ClientsModule {

    @Provides
    @ViewModelScoped
    fun provideClientService(retrofit: Retrofit): ClientsService =
        retrofit.create(ClientsService::class.java)

    @Module
    @InstallIn(ViewModelComponent::class)
    interface ClientRepoBindModule {

        @Binds
        @ViewModelScoped
        fun providesRepo(repoImpl: ClientsRepoImpl): ClientsRepo
    }
}