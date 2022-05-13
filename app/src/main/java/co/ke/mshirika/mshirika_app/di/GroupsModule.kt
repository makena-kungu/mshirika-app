package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.remote.services.GroupsService
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
object GroupsModule {

    @Provides
    @ViewModelScoped
    fun providesGroupsService(retrofit: Retrofit): GroupsService =
        retrofit.create(GroupsService::class.java)
}