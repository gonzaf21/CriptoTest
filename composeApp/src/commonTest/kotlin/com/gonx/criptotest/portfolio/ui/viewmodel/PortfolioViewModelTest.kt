package com.gonx.criptotest.portfolio.ui.viewmodel

import app.cash.turbine.test
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.util.formatFiat
import com.gonx.criptotest.core.util.uiText
import com.gonx.criptotest.portfolio.data.FakePortfolioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PortfolioViewModelTest {
    private lateinit var viewModel: PortfolioViewModel
    private lateinit var repository: FakePortfolioRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        repository = FakePortfolioRepository()
        viewModel = PortfolioViewModel(
            portfolioRepository = repository,
            coroutineDispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `State and portfolio coins are properly combined`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.coins.isEmpty())
            val portfolioCoin = FakePortfolioRepository.portfolioCoin
            repository.savePortfolioCoin(portfolioCoin)

            awaitItem() // Ignore the first emission
            val updatedState = awaitItem()
            assertTrue(updatedState.coins.isNotEmpty())
            assertFalse(updatedState.isLoading)
            assertEquals(
                FakePortfolioRepository.portfolioCoin.coin.id,
                updatedState.coins.first().id
            )

        }
    }

    @Test
    fun `Portfolio value updates when a coin is added`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals(initialState.portfolioValue, formatFiat(10000.0))

            val portfolioCoin = FakePortfolioRepository.portfolioCoin.copy(
                ownedAmountInUnit = 50.0,
                ownedAmountInFiat = 1000.0,
            )
            repository.savePortfolioCoin(portfolioCoin)

            val updatedState = awaitItem()
            assertEquals(updatedState.portfolioValue, formatFiat(11000.0))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Loading state and error message update on failure`() = runTest {
        repository.simulateError()

        viewModel.state.test {
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(errorState.error, DataError.Remote.SERVER.uiText())
        }
    }
}