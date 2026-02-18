package com.gonx.criptotest.coins.data.mapper

import com.gonx.criptotest.coins.data.remote.dto.CoinItemDto
import com.gonx.criptotest.coins.data.remote.dto.CoinPriceHistoryItemDto
import com.gonx.criptotest.coins.domain.model.CoinModel
import com.gonx.criptotest.coins.domain.model.PriceModel
import com.gonx.criptotest.core.coin.Coin

fun CoinItemDto.toCoinModel() = CoinModel(
    coin = Coin(
        id = uuid,
        name = name,
        symbol = symbol,
        iconUrl = iconUrl,
    ),
    price = price,
    change = change,
)

fun CoinPriceHistoryItemDto.toPriceModel() = PriceModel(
    price = price ?: 0.0,
    timestamp = timestamp,
)