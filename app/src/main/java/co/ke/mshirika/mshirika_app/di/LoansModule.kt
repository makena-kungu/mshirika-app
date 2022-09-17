package co.ke.mshirika.mshirika_app.di

import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.services.LoansService
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.loans.LoansRepoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [LoansModule.LoansBindModule::class])
@InstallIn(SingletonComponent::class)
object LoansModule {

    @Provides
    @Singleton
    fun provideLoansService(retrofit: Retrofit): LoansService {
        return retrofit.create(LoansService::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface LoansBindModule {
        @Binds
        @Singleton
        fun bindLoanRepo(loansRepoImpl: LoansRepoImpl): LoansRepo
    }
}