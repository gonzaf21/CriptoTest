package com.gonx.criptotest.portfolio.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserBalanceDao {
    @Query("SELECT cashBalance FROM UserBalanceEntity")
    suspend fun getCashBalance(): Double?

    @Upsert
    suspend fun insertBalance(userBalanceEntity: UserBalanceEntity)

    @Query("UPDATE UserBalanceEntity SET cashBalance = :newBalance")
    suspend fun updateCashBalance(newBalance: Double)
}