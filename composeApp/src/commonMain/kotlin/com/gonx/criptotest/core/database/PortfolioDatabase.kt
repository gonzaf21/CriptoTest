package com.gonx.criptotest.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.gonx.criptotest.portfolio.data.local.PortfolioCoinEntity
import com.gonx.criptotest.portfolio.data.local.PortfolioDao
import com.gonx.criptotest.portfolio.data.local.UserBalanceDao
import com.gonx.criptotest.portfolio.data.local.UserBalanceEntity

@Database(entities = [PortfolioCoinEntity::class, UserBalanceEntity::class], version = 2)
@ConstructedBy(PortfolioDatabaseFactory::class)
abstract class PortfolioDatabase : RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao
    abstract fun userBalanceDao(): UserBalanceDao
}
