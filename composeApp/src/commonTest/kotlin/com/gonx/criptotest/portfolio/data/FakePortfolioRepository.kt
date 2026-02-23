package com.gonx.criptotest.portfolio.data

import com.gonx.criptotest.core.coin.Coin
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.EmptyResult
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.portfolio.domain.PortfolioCoinModel
import com.gonx.criptotest.portfolio.domain.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePortfolioRepository : PortfolioRepository {
    private val _data = MutableStateFlow<Result<List<PortfolioCoinModel>, DataError.Remote>>(
        Result.Success(emptyList())
    )
    private val _cashBalance = MutableStateFlow(cashBalance)
    private val _portfolioValue = MutableStateFlow(portfolioValue)

    private val listOfCoins = mutableListOf<PortfolioCoinModel>()

    override suspend fun initializeBalance() {
        /* no-op */
    }

    override fun allPortfolioCoins(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>> {
        return _data.asStateFlow()
    }

    override suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote> {
        return Result.Success(portfolioCoin)
    }

    override suspend fun savePortfolioCoin(portfolioCoinModel: PortfolioCoinModel): EmptyResult<DataError.Local> {
        listOfCoins.add(portfolioCoinModel)
        _portfolioValue.value = listOfCoins.sumOf { it.ownedAmountInFiat }
        _data.value = Result.Success(listOfCoins)
        return Result.Success(Unit)
    }

    override suspend fun removeCoinFromPortfolio(coinId: String) {
        _data.update { Result.Success(emptyList()) }
    }

    override fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>> {
        return _portfolioValue.map { Result.Success(it) }
    }

    override fun totalBalance(): Flow<Result<Double, DataError.Remote>> {
        return _cashBalance.combine(_portfolioValue) { cash, portfolio ->
            cash + portfolio
        }.map { Result.Success(it) }
    }

    override fun cashBalance(): Flow<Double> {
        return _cashBalance.asStateFlow()
    }

    override suspend fun updateCashBalance(newBalance: Double) {
        _cashBalance.value = newBalance
    }

    fun simulateError() {
        _data.value = Result.Error(DataError.Remote.SERVER)
    }

    companion object {
        val fakeCoin = Coin(
            id = "fakeId",
            name = "fakeCoin",
            symbol = "FAKE",
            iconUrl = "https://fake.url/fake.png",
        )
        val portfolioCoin = PortfolioCoinModel(
            coin = fakeCoin,
            performancePercent = 1000.0,
            averagePurchasePrice = 3000.0,
            ownedAmountInUnit = 10.0,
            ownedAmountInFiat = 10.0
        )
        val cashBalance = 10000.0
        val portfolioValue = 0.0
    }
}