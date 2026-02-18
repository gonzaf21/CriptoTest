package com.gonx.criptotest.portfolio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gonx.criptotest.core.domain.DataError
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.core.util.formatCoinUnit
import com.gonx.criptotest.core.util.formatFiat
import com.gonx.criptotest.core.util.formatPercentage
import com.gonx.criptotest.core.util.uiText
import com.gonx.criptotest.portfolio.domain.PortfolioCoinModel
import com.gonx.criptotest.portfolio.domain.PortfolioRepository
import com.gonx.criptotest.portfolio.domain.toUiPortfolioCoinItem
import com.gonx.criptotest.portfolio.ui.model.PortfolioState
import com.gonx.criptotest.portfolio.ui.model.UiPortfolioCoinItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PortfolioState(isLoading = true))
    val state: StateFlow<PortfolioState> = combine(
        _state,
        portfolioRepository.allPortfolioCoins(),
        portfolioRepository.totalBalance(),
        portfolioRepository.cashBalance(),
    ) { currentState, portfolioCoinsResponse, totalBalanceResult, cashBalance ->
        when (portfolioCoinsResponse) {
            is Result.Success -> {
                handleSuccessState(
                    currentState,
                    portfolioCoinsResponse.data,
                    totalBalanceResult,
                    cashBalance
                )
            }

            is Result.Error -> {
                handleErrorState(currentState, portfolioCoinsResponse.error)
            }
        }
    }.onStart {
        portfolioRepository.initializeBalance()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PortfolioState(isLoading = true),
    )

    private fun handleSuccessState(
        currentState: PortfolioState,
        portfolioCoins: List<PortfolioCoinModel>,
        totalBalanceResult: Result<Double, DataError>,
        cashBalance: Double,
    ): PortfolioState {
        val portfolioValue = when (totalBalanceResult) {
            is Result.Success -> formatFiat(totalBalanceResult.data)
            is Result.Error -> formatFiat(0.0)
        }

        return currentState.copy(
            coins = portfolioCoins.map { it.toUiPortfolioCoinItem() },
            portfolioValue = portfolioValue,
            cashBalance = formatFiat(cashBalance),
            showBuyBtn = portfolioCoins.isNotEmpty(),
            isLoading = false,
        )
    }

    private fun handleErrorState(
        currentState: PortfolioState,
        error: DataError,
    ): PortfolioState {
        return currentState.copy(isLoading = false, error = error.uiText())
    }
}