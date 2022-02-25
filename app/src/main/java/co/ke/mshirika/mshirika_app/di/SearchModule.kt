package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.remote.services.SearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
object SearchModule {

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)
}