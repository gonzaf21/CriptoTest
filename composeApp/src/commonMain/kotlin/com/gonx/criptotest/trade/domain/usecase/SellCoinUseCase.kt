package com.gonx.criptotest.trade.domain.usecase

import com.gonx.criptotest.core.coin.Coin
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.EmptyResult
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.portfolio.domain.PortfolioRepository
import kotlinx.coroutines.flow.first

private const val SELL_ALL_THRESHOLD = 1

class SellCoinUseCase(
    private val portfolioRepository: PortfolioRepository,
) {

    suspend operator fun invoke(
        coin: Coin,
        amountInFiat: Double,
        price: Double,
    ): EmptyResult<DataError> {
        when (val existingCoinResponse = portfolioRepository.getPortfolioCoin(coin.id)) {
            is Result.Success -> {
                val existingCoin = existingCoinResponse.data
                val sellAmountInUnit = amountInFiat / price
                val balance = portfolioRepository.cashBalance().first()

                if (existingCoin == null || existingCoin.ownedAmountInFiat < sellAmountInUnit) {
                    return Result.Error(DataError.Local.INSUFFICIENT_FUNDS)
                }
                val remainingAmountFiat = existingCoin.ownedAmountInFiat - amountInFiat
                val remainingAmountUnit = existingCoin.ownedAmountInUnit - sellAmountInUnit
                if (remainingAmountFiat < SELL_ALL_THRESHOLD) {
                    portfolioRepository.removeCoinFromPortfolio(coin.id)
                } else {
                    portfolioRepository.savePortfolioCoin(
                        existingCoin.copy(
                            ownedAmountInFiat = remainingAmountFiat,
                            ownedAmountInUnit = remainingAmountUnit,
                        )
                    )
                }
                portfolioRepository.updateCashBalance(balance + amountInFiat)
                return Result.Success(Unit)
            }

            is Result.Error -> {
                return existingCoinResponse
            }
        }
    }
}