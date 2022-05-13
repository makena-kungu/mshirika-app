package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.pagingSource.LoansPagingSource
import co.ke.mshirika.mshirika_app.data_layer.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module(includes = [LoansModule.LoansBindModule::class])
@InstallIn(ViewModelComponent::class)
object LoansModule {

    @Provides
    @ViewModelScoped
    fun provideLoansService(retrofit: Retrofit): LoansService {
        return retrofit.create(LoansService::class.java)
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    interface LoansBindModule{
        @Binds
        @ViewModelScoped
        fun bindLoanRepo(loansRepoImpl: LoansRepoImpl):LoansRepo
    }
}