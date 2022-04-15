package co.ke.mshirika.mshirika_app.di

import android.content.Context
import co.ke.mshirika.mshirika_app.utility.PreferencesStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    @ActivityScoped
    fun providePrefRepository(
        @ApplicationContext context: Context
    ): PreferencesStoreRepository {
        return PreferencesStoreRepository(context)
    }
}