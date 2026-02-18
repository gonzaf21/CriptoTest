package com.gonx.criptotest.di

import androidx.room.RoomDatabase
import com.gonx.criptotest.coins.data.remote.impl.KtorCoinsRemoteDataSource
import com.gonx.criptotest.coins.domain.api.CoinsRemoteDataSource
import com.gonx.criptotest.coins.domain.usecases.GetCoinDetailsUseCase
import com.gonx.criptotest.coins.domain.usecases.GetCoinPriceHistoryUseCase
import com.gonx.criptotest.coins.domain.usecases.GetCoinsListUseCase
import com.gonx.criptotest.coins.ui.viewmodel.CoinsListViewModel
import com.gonx.criptotest.core.database.PortfolioDatabase
import com.gonx.criptotest.core.database.getPortfolioDatabase
import com.gonx.criptotest.core.network.HttpClientFactory
import com.gonx.criptotest.portfolio.data.PortfolioRepositoryImpl
import com.gonx.criptotest.portfolio.domain.PortfolioRepository
import com.gonx.criptotest.portfolio.ui.viewmodel.PortfolioViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            platformModule,
        )
    }

expect val platformModule: Module

val sharedModule: Module = module {
    // core
    single<HttpClient> { HttpClientFactory.create(get()) }
    // portfolio
    single { getPortfolioDatabase(get<RoomDatabase.Builder<PortfolioDatabase>>()) }
    singleOf(::PortfolioRepositoryImpl).bind<PortfolioRepository>()
    single { get<PortfolioDatabase>().portfolioDao() }
    single { get<PortfolioDatabase>().userBalanceDao() }
    viewModel { PortfolioViewModel(get()) }
    // coins list
    viewModel { CoinsListViewModel(get(), get()) }
    singleOf(::GetCoinsListUseCase)
    singleOf(::KtorCoinsRemoteDataSource).bind<CoinsRemoteDataSource>()
    singleOf(::GetCoinDetailsUseCase)
    singleOf(::GetCoinPriceHistoryUseCase)
}