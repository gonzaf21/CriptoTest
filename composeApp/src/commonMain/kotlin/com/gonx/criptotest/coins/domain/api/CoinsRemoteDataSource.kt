package com.gonx.criptotest.coins.domain.api

import com.gonx.criptotest.coins.data.remote.dto.CoinDetailsResponseDto
import com.gonx.criptotest.coins.data.remote.dto.CoinPriceHistoryResponseDto
import com.gonx.criptotest.coins.data.remote.dto.CoinsResponseDto
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.Result

interface CoinsRemoteDataSource {
    suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote>
    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote>
    suspend fun getCoinById(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote>
}