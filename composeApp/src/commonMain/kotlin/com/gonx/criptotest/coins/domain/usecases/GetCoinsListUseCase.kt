package com.gonx.criptotest.coins.domain.usecases

import com.gonx.criptotest.coins.data.mapper.toCoinModel
import com.gonx.criptotest.coins.domain.api.CoinsRemoteDataSource
import com.gonx.criptotest.coins.domain.model.CoinModel
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.core.domain.map

class GetCoinsListUseCase(private val client: CoinsRemoteDataSource) {
    suspend fun execute(): Result<List<CoinModel>, DataError.Remote> {
        return client.getListOfCoins().map { dto ->
            dto.data.coins.map { it.toCoinModel() }
        }
    }
}