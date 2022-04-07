package co.ke.mshirika.mshirika_app.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import co.ke.mshirika.mshirika_app.ui.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    @ActivityScoped
    fun provideAuthKey(@ActivityContext context: Context):String {
        val viewModel = ViewModelProvider(context as FragmentActivity)[MainViewModel::class.java]
        return viewModel.key
    }
}