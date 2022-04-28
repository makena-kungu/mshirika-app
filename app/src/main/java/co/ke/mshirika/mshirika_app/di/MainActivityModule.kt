package co.ke.mshirika.mshirika_app.di

import android.content.Context
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainActivityModule {

    @Provides
    @Singleton
    fun providePrefRepository(
        @ApplicationContext context: Context
    ): PreferencesStoreRepository {
        return PreferencesStoreRepository(context)
    }
}