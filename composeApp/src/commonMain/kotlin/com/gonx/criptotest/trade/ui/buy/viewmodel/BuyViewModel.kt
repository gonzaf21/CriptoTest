package com.gonx.criptotest.trade.ui.buy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gonx.criptotest.coins.domain.usecase.GetCoinDetailsUseCase
import com.gonx.criptotest.core.domain.Result
import com.gonx.criptotest.core.util.formatFiat
import com.gonx.criptotest.core.util.uiText
import com.gonx.criptotest.portfolio.domain.PortfolioRepository
import com.gonx.criptotest.trade.domain.usecase.BuyCoinUseCase
import com.gonx.criptotest.trade.mapper.toCoin
import com.gonx.criptotest.trade.ui.buy.model.TradeState
import com.gonx.criptotest.trade.ui.common.model.UiTradeCoinItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BuyViewModel(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val buyCoinUseCase: BuyCoinUseCase
) : ViewModel() {
    private val tempCoinId = "1" // TODO: Will be removed when passed as safe arg
    private val _amount = MutableStateFlow("")
    private val _state = MutableStateFlow(TradeState())
    private val state = combine(
        _state,
        _amount,
    ) { cState, amount ->
        cState.copy(
            amount = amount,
        )
    }.onStart {
        val balance = portfolioRepository.cashBalance().first()
        getCoinsDetails(balance)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TradeState(isLoading = true)
    )

    private suspend fun getCoinsDetails(balance: Double) {
        when (val coinResponse = getCoinDetailsUseCase.execute(tempCoinId)) {
            is Result.Success -> {
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id = coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price
                        ),
                        availableAmount = "Available: ${formatFiat(balance)}"
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

    fun onBuyClick() {
        val tradeCoin = state.value.coin ?: return
        viewModelScope.launch {
            val buyCoinResponse = buyCoinUseCase(
                coin = tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price,
            )

            when (buyCoinResponse) {
                is Result.Success -> {
                    // TODO: Navigate to next screen with event
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = buyCoinResponse.error.uiText()
                        )
                    }
                }
            }
        }
    }
}