package com.gonx.criptotest.coins.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gonx.criptotest.coins.domain.usecase.GetCoinPriceHistoryUseCase
import com.gonx.criptotest.coins.domain.usecase.GetCoinsListUseCase
import com.gonx.criptotest.coins.ui.model.CoinsState
import com.gonx.criptotest.coins.ui.model.UiChartState
import com.gonx.criptotest.coins.ui.model.UiCoinListItem
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.core.util.formatFiat
import com.gonx.criptotest.core.util.formatPercentage
import com.gonx.criptotest.core.util.uiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinsListViewModel(
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val getCoinsPriceHistoryUseCase: GetCoinPriceHistoryUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CoinsState())
    val state = _state
        .onStart { getAllCoins() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = CoinsState(),
        )

    private suspend fun getAllCoins() {
        when (val response = getCoinsListUseCase.execute()) {
            is Result.Success -> _state.update {
                CoinsState(coins = response.data.map {
                    UiCoinListItem(
                        id = it.coin.id,
                        name = it.coin.name,
                        symbol = it.coin.symbol,
                        iconUrl = it.coin.iconUrl,
                        formattedPrice = formatFiat(it.price),
                        formattedChange = formatPercentage(it.change),
                        isPositive = it.change >= 0,
                    )
                })
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        coins = emptyList(),
                        error = response.error.uiText()
                    )
                }
            }
        }
    }

    fun onCoinLongPress(coinId: String) {
        _state.update {
            it.copy(
                chartState = UiChartState(
                    sparkLine = emptyList(),
                    isLoading = true,
                )
            )
        }

        viewModelScope.launch {
            when (val priceHistory = getCoinsPriceHistoryUseCase.execute(coinId)) {
                is Result.Success -> _state.update { cState ->
                    cState.copy(
                        chartState = UiChartState(
                            sparkLine = priceHistory.data
                                .sortedBy { it.timestamp }
                                .map { it.price },
                            isLoading = false,
                            coinName = state.value.coins.firstOrNull { it.id == coinId }?.name.orEmpty(),
                        ),
                    )
                }

                is Result.Error -> {
                    _state.update { cState ->
                        cState.copy(
                            chartState = UiChartState(
                                sparkLine = emptyList(),
                                isLoading = false,
                                coinName = "",
                            ),
                        )
                    }
                }
            }
        }
    }

    fun onDismissChart() {
        _state.update { it.copy(chartState = null) }
    }
}