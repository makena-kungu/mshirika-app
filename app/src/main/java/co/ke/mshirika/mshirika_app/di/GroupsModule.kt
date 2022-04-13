package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.remote.services.GroupsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GroupsModule {

    @Provides
    @Singleton
    fun providesGroupsService(retrofit: Retrofit): GroupsService =
        retrofit.create(GroupsService::class.java)
}