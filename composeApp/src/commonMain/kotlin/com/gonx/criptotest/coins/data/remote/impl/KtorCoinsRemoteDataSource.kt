package com.gonx.criptotest.coins.data.remote.impl

import com.gonx.criptotest.coins.data.remote.dto.CoinDetailsResponseDto
import com.gonx.criptotest.coins.data.remote.dto.CoinPriceHistoryResponseDto
import com.gonx.criptotest.coins.data.remote.dto.CoinsResponseDto
import com.gonx.criptotest.coins.domain.api.CoinsRemoteDataSource
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.core.network.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val BASE_URL = "https://api.coinranking.com/v2" // TODO: Set in gradle to obtain from BuildConfig

class KtorCoinsRemoteDataSource (
    private val httpClient: HttpClient
): CoinsRemoteDataSource {
    override suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/coins")
        }
    }

    override suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/coin/$coinId/price-history")
        }
    }

    override suspend fun getCoinById(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/coin/$coinId")
        }
    }

}
