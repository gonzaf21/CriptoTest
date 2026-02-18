package com.gonx.criptotest.coins.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinPriceHistoryResponseDto(
    val data: CoinPriceHistoryDto,
)

@Serializable
data class CoinPriceHistoryDto(
    val history: List<CoinPriceHistoryItemDto>,
)

@Serializable
data class CoinPriceHistoryItemDto(
    val price: Double?,
    val timestamp: Long,
)
