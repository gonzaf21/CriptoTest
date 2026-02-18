package com.gonx.criptotest.portfolio.domain

import com.gonx.criptotest.core.coin.Coin
import com.gonx.criptotest.core.util.formatCoinUnit
import com.gonx.criptotest.core.util.formatFiat
import com.gonx.criptotest.core.util.formatPercentage
import com.gonx.criptotest.portfolio.ui.model.UiPortfolioCoinItem

data class PortfolioCoinModel(
    val coin: Coin,
    val performancePercent: Double,
    val averagePurchasePrice: Double,
    val ownedAmountInUnit: Double,
    val ownedAmountInFiat: Double,
)

fun PortfolioCoinModel.toUiPortfolioCoinItem(): UiPortfolioCoinItem {
    return UiPortfolioCoinItem(
        id = coin.id,
        name = coin.name,
        iconUrl = coin.iconUrl,
        amountInUnitText = formatCoinUnit(ownedAmountInUnit, coin.symbol),
        amountInFiatText = formatFiat(ownedAmountInFiat),
        performancePercentText = formatPercentage(performancePercent),
        isPositive = performancePercent >= 0,
    )
}