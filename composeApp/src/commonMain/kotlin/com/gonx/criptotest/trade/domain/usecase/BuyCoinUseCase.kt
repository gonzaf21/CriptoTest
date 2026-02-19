package com.gonx.criptotest.trade.domain.usecase

import com.gonx.criptotest.core.coin.Coin
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.EmptyResult
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.portfolio.domain.PortfolioCoinModel
import com.gonx.criptotest.portfolio.domain.PortfolioRepository
import kotlinx.coroutines.flow.first

class BuyCoinUseCase(
    private val portfolioRepository: PortfolioRepository,
) {
    suspend operator fun invoke(
        coin: Coin,
        amountInFiat: Double,
        price: Double,
    ): EmptyResult<DataError> {
        val balance = portfolioRepository.cashBalance().first()
        if (balance < amountInFiat) {
            return Result.Error(DataError.Local.INSUFFICIENT_FUNDS)
        }

        val existingCoinResult = portfolioRepository.getPortfolioCoin(coin.id)
        val existingCoin = when (existingCoinResult) {
            is Result.Success -> existingCoinResult.data
            is Result.Error -> return Result.Error(existingCoinResult.error)
        }
        val amountInUnit = amountInFiat / price
        if (existingCoin != null) {
            val newAmountOwned = existingCoin.ownedAmountInUnit + amountInUnit
            val newTotalInvestment = existingCoin.ownedAmountInFiat + amountInFiat
            val newAveragePurchasePrice = newTotalInvestment / newAmountOwned

            portfolioRepository.savePortfolioCoin(
                existingCoin.copy(
                    ownedAmountInUnit = newAmountOwned,
                    ownedAmountInFiat = newTotalInvestment,
                    averagePurchasePrice = newAveragePurchasePrice,
                )
            )
        } else {
            portfolioRepository.savePortfolioCoin(
                PortfolioCoinModel(
                    coin = coin,
                    performancePercent = 0.0,
                    averagePurchasePrice = price,
                    ownedAmountInUnit = amountInUnit,
                    ownedAmountInFiat = amountInFiat,
                )
            )
        }
        portfolioRepository.updateCashBalance(balance - amountInFiat)
        return Result.Success(Unit)
    }
}