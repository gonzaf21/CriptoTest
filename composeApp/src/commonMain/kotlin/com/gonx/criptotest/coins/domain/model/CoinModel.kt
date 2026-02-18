package com.gonx.criptotest.coins.domain.model

import com.gonx.criptotest.core.coin.Coin

data class CoinModel(
    val coin: Coin,
    val price: Double,
    val change: Double,
)