package com.gonx.criptotest.coins.domain.usecase

import com.gonx.criptotest.coins.data.mapper.toPriceModel
import com.gonx.criptotest.coins.domain.api.CoinsRemoteDataSource
import com.gonx.criptotest.coins.domain.model.PriceModel
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.core.domain.map

class GetCoinPriceHistoryUseCase (private val client: CoinsRemoteDataSource) {
    suspend fun execute(coinId:String): Result<List<PriceModel>, DataError.Remote> {
        return client.getPriceHistory(coinId).map { dto ->
            dto.data.history.map { it.toPriceModel() }
        }
    }
}