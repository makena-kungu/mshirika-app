package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.remote.services.GroupsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object GroupsModule {

    @Provides
    fun providesGroupsService(retrofit: Retrofit): GroupsService =
        retrofit.create(GroupsService::class.java)
}