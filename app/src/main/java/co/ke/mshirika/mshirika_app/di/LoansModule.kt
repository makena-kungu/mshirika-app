package co.ke.mshirika.mshirika_app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LoansModule {

    @Provides
    @ViewModelScoped
    fun doSomething():String? = null
}