package com.gonx.criptotest.trade.ui.sell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gonx.criptotest.coins.domain.usecase.GetCoinDetailsUseCase
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.core.util.formatFiat
import com.gonx.criptotest.core.util.uiText
import com.gonx.criptotest.portfolio.domain.PortfolioRepository
import com.gonx.criptotest.trade.domain.usecase.SellCoinUseCase
import com.gonx.criptotest.trade.mapper.toCoin
import com.gonx.criptotest.trade.ui.common.model.TradeState
import com.gonx.criptotest.trade.ui.common.model.UiTradeCoinItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SellViewModel(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val sellCoinUseCase: SellCoinUseCase
) : ViewModel() {
    private val tempCoinId = "1" // TODO: Will be removed when passed as safe arg
    private val _amount = MutableStateFlow("")
    private val _state = MutableStateFlow(TradeState())
    val state = combine(
        _state,
        _amount,
    ) { cState, amount ->
        cState.copy(
            amount = amount,
        )
    }.onStart {
        when (val portfolioCoinResponse = portfolioRepository.getPortfolioCoin(tempCoinId)) {
            is Result.Success -> {
                portfolioCoinResponse.data?.ownedAmountInUnit?.let {
                    getCoinsDetails(it)
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = portfolioCoinResponse.error.uiText()
                    )
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TradeState(isLoading = true)
    )

    private suspend fun getCoinsDetails(ownedAmountInUnit: Double) {
        when (val coinResponse = getCoinDetailsUseCase.execute(tempCoinId)) {
            is Result.Success -> {
                val availableAmountInFiat = ownedAmountInUnit * coinResponse.data.price
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id = coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price,
                        ),
                        availableAmount = "Available: ${formatFiat(availableAmountInFiat)}"
                    )
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = coinResponse.error.uiText()
                    )
                }
            }
        }
    }

    fun onAmountChange(amount: String) {
        _amount.value = amount
    }

    fun onSellClick() {
        val tradeCoin = state.value.coin ?: return
        viewModelScope.launch {
            val sellCoinResponse = sellCoinUseCase(
                coin = tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price,
            )

            when (sellCoinResponse) {
                is Result.Success -> {
                    // TODO: Navigate to next screen with event
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = sellCoinResponse.error.uiText()
                        )
                    }
                }
            }
        }
    }
}