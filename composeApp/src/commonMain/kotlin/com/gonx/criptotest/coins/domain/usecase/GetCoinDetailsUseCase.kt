package com.gonx.criptotest.coins.domain.usecase

import com.gonx.criptotest.coins.data.mapper.toCoinModel
import com.gonx.criptotest.coins.domain.api.CoinsRemoteDataSource
import com.gonx.criptotest.coins.domain.model.CoinModel
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.core.domain.map

class GetCoinDetailsUseCase (private val client: CoinsRemoteDataSource) {
    suspend fun execute(coinId:String): Result<CoinModel, DataError.Remote> {
        return client.getCoinById(coinId).map { dto ->
            dto.data.coin.toCoinModel()
        }
    }
}