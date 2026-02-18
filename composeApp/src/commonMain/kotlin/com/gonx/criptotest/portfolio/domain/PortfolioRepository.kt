package com.gonx.criptotest.portfolio.domain

import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.EmptyResult
import com.gonx.criptotest.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    suspend fun initializeBalance()
    fun allPortfolioCoins(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>>
    suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote>
    suspend fun savePortfolioCoin(portfolioCoinModel: PortfolioCoinModel): EmptyResult<DataError.Local>
    suspend fun removeCoinFromPortfolio(coinId: String)

    fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>>
    fun totalBalance(): Flow<Result<Double, DataError.Remote>>
    fun cashBalance(): Flow<Double>
    suspend fun updateCashBalance(newBalance: Double)
}